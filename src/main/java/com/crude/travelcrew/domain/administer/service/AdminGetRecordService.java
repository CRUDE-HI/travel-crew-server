package com.crude.travelcrew.domain.administer.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.administer.dto.RecordListReq;
import com.crude.travelcrew.domain.administer.dto.RecordListRes;
import com.crude.travelcrew.domain.administer.dto.RecordListResponseDto;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
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

		RecordListRes recordListRes = new RecordListRes(convertToDto(page.getContent()), (int) page.getTotalElements(),
			recordListReq.getPage());

		return recordListRes;
	}
}
