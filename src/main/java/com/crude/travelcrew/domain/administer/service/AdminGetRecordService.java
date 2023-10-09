package com.crude.travelcrew.domain.administer.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.administer.dto.getRecord.ADGetRecordRes;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListReq;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListRes;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListResponseDto;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordComment;
import com.crude.travelcrew.domain.record.model.entity.RecordImage;
import com.crude.travelcrew.domain.record.repository.RecordCommentRepository;
import com.crude.travelcrew.domain.record.repository.RecordImageRepository;
import com.crude.travelcrew.domain.record.repository.RecordRepository;

@Service
public class AdminGetRecordService {

	@Autowired
	RecordRepository recordRepository;

	@Autowired
	private RecordImageRepository recordImageRepository;

	@Autowired
	RecordCommentRepository recordCommentRepository;

	public List<ADRecordListResponseDto> convertToDto(List<Record> records) {
		return records.stream()
			.map(
				record -> {
					Member member = record.getMember();
					return new ADRecordListResponseDto(
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
	public ADRecordListRes getList(ADRecordListReq ADRecordListReq) {

		Page<Record> page = recordRepository.findAll(ADRecordListReq.pageable());

		ADRecordListRes ADRecordListRes = new ADRecordListRes(convertToDto(page.getContent()), (int)page.getTotalElements(),
			ADRecordListReq.getPage());

		return ADRecordListRes;
	}

	@Transactional
	public ADGetRecordRes getRecord(Long recordId) {
		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		List<String> imageUrls = record.getRecordImages().stream()
			.map(RecordImage::getImageUrl)
			.collect(Collectors.toList());

		Member member = record.getMember();

		return new ADGetRecordRes(
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

	@Transactional
	public void blockAndDeleteImages(Long recordId) {
		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		record.blockContent();

		recordImageRepository.deleteAllByRecordId(recordId);

		recordRepository.save(record);
	}

	public void blockComment(Long recordId, Long commentId) {
		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		RecordComment recordComment = recordCommentRepository.findByRecordAndId(record, commentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		recordComment.blockContent();

		recordCommentRepository.save(recordComment);
	}
}
