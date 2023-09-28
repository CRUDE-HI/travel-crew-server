package com.crude.travelcrew.domain.travelrecord.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.ArrayList;
import java.util.List;
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
}
