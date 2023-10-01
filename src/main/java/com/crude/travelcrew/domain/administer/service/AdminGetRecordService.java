package com.crude.travelcrew.domain.administer.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.administer.dto.GetRecordRes;
import com.crude.travelcrew.domain.administer.dto.RecordListReq;
import com.crude.travelcrew.domain.administer.dto.RecordListRes;
import com.crude.travelcrew.domain.administer.dto.RecordListResponseDto;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordImage;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordRepository;

@Service
public class AdminGetRecordService {

	@Autowired
	TravelRecordRepository travelRecordRepository;

	public List<RecordListResponseDto> convertToDto(List<TravelRecord> records) {
		return records.stream()
			.map(
				record -> {
					Member member = record.getMember();
					return new RecordListResponseDto(
						record.getId(),
						record.getTitle(),
						member.getNickname(),
						record.getCreatedAt(),
						record.getUpdatedAt()
					);
				}
			).collect(Collectors.toList());
	}

	@Transactional
	public RecordListRes getList(RecordListReq recordListReq) {

		Page<TravelRecord> page = travelRecordRepository.findAll(recordListReq.pageable());

		RecordListRes recordListRes = new RecordListRes(convertToDto(page.getContent()), (int)page.getTotalElements(),
			recordListReq.getPage());

		return recordListRes;
	}

	@Transactional
	public GetRecordRes getRecord(Long recordId) {
		TravelRecord record = travelRecordRepository.findById(recordId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		List<String> imageUrls = record.getTravelRecordImages().stream()
			.map(TravelRecordImage::getImageUrl)
			.collect(Collectors.toList());

		Member member = record.getMember();

		return new GetRecordRes(
			record.getId(),
			record.getTitle(),
			record.getContent(),
			record.getCreatedAt(),
			record.getUpdatedAt(),
			imageUrls,
			member.getEmail(),
			member.getNickname(),
			member.getProfileImgUrl()
		);
	}
}
