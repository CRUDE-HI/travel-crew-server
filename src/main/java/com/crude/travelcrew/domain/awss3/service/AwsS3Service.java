package com.crude.travelcrew.domain.awss3.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

	/**
	 * 이미지 파일 업로드
	 */
	String uploadImageFile(MultipartFile image, String dir);

	/**
	 * 다중 이미지 파일 업로드
	 */
	List<String> uploadImageFileList(List<MultipartFile> images, String dir);
}
