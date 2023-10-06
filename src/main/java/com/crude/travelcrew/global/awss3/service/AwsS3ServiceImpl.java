package com.crude.travelcrew.global.awss3.service;

import static com.crude.travelcrew.global.error.type.CommonErrorCode.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.crude.travelcrew.global.awss3.constants.FileExtension;
import com.crude.travelcrew.global.error.exception.CommonException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	@Override
	public String uploadImageFile(MultipartFile image, String dir) {

		String fileName = createFileName(Objects.requireNonNull(image.getOriginalFilename()));

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(image.getSize());
		objectMetadata.setContentType(image.getContentType());

		try (InputStream inputStream = image.getInputStream()) {

			amazonS3.putObject(new PutObjectRequest(bucket + "/" + dir, fileName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));

		} catch (IOException | AmazonClientException e) {
			log.error("image upload fail : {}", e.getMessage());
			throw new CommonException(FAIL_TO_UPLOAD_IMAGE);
		}
		log.info("image upload success");
		return amazonS3.getUrl(bucket, dir + "/" + fileName).toString();
	}

	@Override
	public List<String> uploadImageFileList(List<MultipartFile> images, String dir) {
		List<String> imageUrlList = new ArrayList<>();

		images.forEach((image) ->
			imageUrlList.add(uploadImageFile(image, dir))
		);

		return imageUrlList;
	}

	@Override
	public void deleteImageFile(String imageUrl, String dir) {
		try {
			amazonS3.deleteObject(bucket, dir + "/" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
			log.info("image delete success");
		} catch (AmazonServiceException e) {
			log.error(e.getMessage());
			throw new CommonException(FAIL_TO_DELETE_IMAGE);
		}
	}

	/**
	 * 파일 업로드 시 파일명 생성
	 */
	private String createFileName(String fileName) {
		return UUID.randomUUID().toString()
			.concat(getFileExtension(fileName));
	}

	/**
	 * 파일 확장자 추출
	 */
	private String getFileExtension(String fileName) {

		List<String> extensions = Stream.of(FileExtension.values())
			.map(Enum::name)
			.collect(Collectors.toList());

		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

		if (!extensions.contains(fileExtension)) {
			log.error("file extension is not valid");
			throw new CommonException(INVALID_IMAGE_TYPE);
		}

		return fileName.substring(fileName.lastIndexOf("."));
	}
}
