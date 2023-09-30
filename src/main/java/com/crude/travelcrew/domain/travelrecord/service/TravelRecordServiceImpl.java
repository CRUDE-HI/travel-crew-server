package com.crude.travelcrew.domain.travelrecord.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.TravelRecordErrorCode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.awss3.service.AwsS3Service;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordRes;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordImage;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordImageRepository;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.exception.TravelRecordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelRecordServiceImpl implements TravelRecordService {

	private final static String DIR = "record";

	private final MemberRepository memberRepository;
	private final TravelRecordRepository travelRecordRepository;
	private final TravelRecordImageRepository travelRecordImageRepository;
	private final AwsS3Service awsS3Service;

	@Override
	@Transactional
	public EditTravelRecordRes createTravelRecord(EditTravelRecordReq request, List<MultipartFile> images,
		String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		TravelRecord travelRecord = TravelRecord.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.member(member)
			.build();

		travelRecordRepository.save(travelRecord);

		if (Objects.isNull(images)) {
			return EditTravelRecordRes.fromEntity(travelRecord);
		}

		List<TravelRecordImage> travelRecordImages = new ArrayList<>();
		List<String> imageUrls = awsS3Service.uploadImageFileList(images, DIR);

		imageUrls.forEach((url) -> {
			TravelRecordImage image = TravelRecordImage.builder()
				.imageUrl(url)
				.travelRecord(travelRecord)
				.build();

			travelRecordImages.add(travelRecordImageRepository.save(image));
		});

		return EditTravelRecordRes.fromEntity(travelRecord, travelRecordImages);
	}

	@Override
	@Transactional
	public Map<String, String> deleteTravelRecord(Long travelRecordId, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		TravelRecord travelRecord = travelRecordRepository.findById(travelRecordId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_NOT_FOUND));

		if (!Objects.equals(travelRecord.getMember().getEmail(), email)) {
			throw new TravelRecordException(FAIL_TO_DELETE_TRAVEL_RECORD);
		}

		List<TravelRecordImage> travelRecordImages
			= travelRecordImageRepository.findAllByTravelRecord(travelRecord);

		if (!travelRecordImages.isEmpty()) {
			// 여행 기록 이미지 aws s3에서 삭제
			travelRecordImages.forEach((image) -> awsS3Service.deleteImageFile(image.getImageUrl(), DIR));
			// 여행 기록 이미지 일괄 삭제
			travelRecordImageRepository.deleteAllByTravelRecordId(travelRecordId);
		}
		// 여행 기록 삭제
		travelRecordRepository.deleteById(travelRecordId);
		return getMessage("여행 기록이 삭제되었습니다.");
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
