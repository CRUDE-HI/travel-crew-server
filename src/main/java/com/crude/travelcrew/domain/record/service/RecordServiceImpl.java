package com.crude.travelcrew.domain.record.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.RecordErrorCode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.record.model.dto.EditRecordReq;
import com.crude.travelcrew.domain.record.model.dto.EditRecordRes;
import com.crude.travelcrew.domain.record.model.dto.GetRecordRes;
import com.crude.travelcrew.domain.record.model.dto.RecordImageRes;
import com.crude.travelcrew.domain.record.model.dto.RecordListRes;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordImage;
import com.crude.travelcrew.domain.record.repository.RecordCommentRepository;
import com.crude.travelcrew.domain.record.repository.RecordHeartRepository;
import com.crude.travelcrew.domain.record.repository.RecordImageRepository;
import com.crude.travelcrew.domain.record.repository.RecordRepository;
import com.crude.travelcrew.global.awss3.service.AwsS3Service;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.exception.RecordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

	private final static String DIR = "record";

	private final MemberRepository memberRepository;
	private final RecordRepository recordRepository;
	private final RecordImageRepository recordImageRepository;
	private final RecordCommentRepository recordCommentRepository;
	private final AwsS3Service awsS3Service;
	private final RecordHeartRepository recordHeartRepository;

	@Override
	@Transactional
	public GetRecordRes getRecord(Long recordId, String email) {
		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		List<String> imageUrls = record.getRecordImages().stream()
			.map(RecordImage::getImageUrl)
			.collect(Collectors.toList());

		long heartsCount = recordRepository.countHeartsForRecord(recordId);

		Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		return new GetRecordRes(
			record.getId(),
			record.getMember().getNickname(),
			record.getMember().getProfileImgUrl(),
			record.getMember().getMemberProfile().getHeartBeat(),
			record.getTitle(),
			record.getContent(),
			imageUrls,
			heartsCount,
			record.getCreatedAt(),
			record.getUpdatedAt(),
			record.getMember().getId() == member.getId()
		);
	}

	@Override
	@Transactional
	public Map<String, Object> listRecord(String keyword, Pageable pageable) {
		Page<Record> page = recordRepository.findByKeyword(keyword, pageable);
		List<Record> content = page.getContent();
		List<RecordListRes> recordList = content.stream()
			.map(record -> {
				long heartsCount = recordHeartRepository.countHeartsForRecord(record.getId());
				RecordListRes recordRes = RecordListRes.getEntity(record);
				recordRes.setHeartsCount(heartsCount);
				return recordRes;
			})
			.collect(Collectors.toList());
		Map<String, Object> result = new HashMap<>();
		result.put("content", recordList);
		result.put("totalPages", page.getTotalPages());
		return result;
	}

	@Override
	@Transactional
	public EditRecordRes addRecord(EditRecordReq request, List<MultipartFile> images,
		String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Record record = Record.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.member(member)
			.build();

		recordRepository.save(record);

		if (Objects.isNull(images)) {
			return EditRecordRes.fromEntity(record);
		}

		List<RecordImage> recordImages = new ArrayList<>();
		List<String> imageUrls = awsS3Service.uploadImageFileList(images, DIR);

		imageUrls.forEach((url) -> {
			RecordImage image = RecordImage.builder()
				.imageUrl(url)
				.record(record)
				.build();

			recordImages.add(recordImageRepository.save(image));
		});

		return EditRecordRes.fromEntity(record, recordImages);
	}

	@Override
	@Transactional
	public Map<String, String> deleteRecord(Long recordId, String email) {

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		if (!Objects.equals(record.getMember().getEmail(), email)) {
			throw new RecordException(FAIL_TO_DELETE_TRAVEL_RECORD);
		}

		List<RecordImage> recordImages
			= recordImageRepository.findAllByRecord(record);

		if (!recordImages.isEmpty()) {
			// 여행 기록 이미지 aws s3에서 삭제
			recordImages.forEach((image) -> awsS3Service.deleteImageFile(image.getImageUrl(), DIR));
			// 여행 기록 이미지 일괄 삭제
			recordImageRepository.deleteAllByRecordId(recordId);
		}
		// 여행 기록 댓글 삭제
		recordCommentRepository.deleteAllByRecordId(recordId);
		// 여행 기록 삭제
		recordRepository.deleteById(recordId);
		return getMessage("여행 기록이 삭제되었습니다.");
	}

	@Override
	@Transactional
	public EditRecordRes updateRecord(Long recordId, EditRecordReq request, String email) {

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		if (!Objects.equals(record.getMember().getEmail(), email)) {
			throw new RecordException(FAIL_TO_UPDATE_TRAVEL_RECORD);
		}

		List<RecordImage> recordImages
			= recordImageRepository.findAllByRecord(record);

		// 제목과 내용 수정
		record.update(request.getTitle(), request.getContent());
		return EditRecordRes.fromEntity(record, recordImages);
	}

	@Override
	@Transactional
	public RecordImageRes addRecordImage(Long recordId, MultipartFile image) {

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		String imageUrl = awsS3Service.uploadImageFile(image, DIR);

		RecordImage recordImage = RecordImage.builder()
			.record(record)
			.imageUrl(imageUrl)
			.build();

		recordImageRepository.save(recordImage);
		return RecordImageRes.fromEntity(recordImage);
	}

	@Override
	@Transactional
	public Map<String, String> removeRecordImage(Long recordId, Long recordImageId) {

		if (!recordRepository.existsById(recordId)) {
			throw new RecordException(TRAVEL_RECORD_NOT_FOUND);
		}

		RecordImage recordImage = recordImageRepository.findById(recordImageId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_IMAGE_NOT_FOUND));

		awsS3Service.deleteImageFile(recordImage.getImageUrl(), DIR);
		recordImageRepository.deleteById(recordImageId);

		return getMessage("여행 기록 이미지가 삭제되었습니다.");
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
