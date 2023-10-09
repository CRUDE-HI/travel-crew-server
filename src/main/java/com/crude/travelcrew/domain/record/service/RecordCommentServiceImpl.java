package com.crude.travelcrew.domain.record.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.RecordErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.record.model.dto.RecordCommentListRes;
import com.crude.travelcrew.domain.record.model.dto.RecordCommentReq;
import com.crude.travelcrew.domain.record.model.dto.RecordCommentRes;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordComment;
import com.crude.travelcrew.domain.record.repository.RecordCommentRepository;
import com.crude.travelcrew.domain.record.repository.RecordRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.exception.RecordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordCommentServiceImpl implements RecordCommentService {

	private final MemberRepository memberRepository;
	private final RecordRepository recordRepository;
	private final RecordCommentRepository recordCommentRepository;

	@Override
	@Transactional
	public RecordCommentListRes getCommentList(Long recordId, Pageable pageable) {
		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		List<RecordComment> list = recordCommentRepository.findByRecord(record, pageable);

		return RecordCommentListRes.fromEntityList(list);
	}

	@Override
	@Transactional
	public RecordCommentRes addRecordComment(Long recordId, RecordCommentReq request, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		RecordComment recordComment = RecordComment.builder()
			.content(request.getContent())
			.record(record)
			.member(member)
			.build();

		recordCommentRepository.save(recordComment);
		return RecordCommentRes.fromEntity(recordComment);
	}

	@Override
	@Transactional
	public Map<String, String> removeRecordComment(Long recordId, Long recordCommentId, String email) {

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		RecordComment recordComment
			= recordCommentRepository.findByRecordAndId(record, recordCommentId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_COMMENT_NOT_FOUND));

		if (!Objects.equals(recordComment.getMember().getEmail(), email)) {
			throw new RecordException(FAIL_TO_DELETE_TRAVEL_COMMENT);
		}

		recordCommentRepository.deleteById(recordCommentId);
		return getMessage("여행 기록 댓글이 삭제되었습니다.");
	}

	@Override
	@Transactional
	public RecordCommentRes updateRecordComment(Long recordId, Long recordCommentId, RecordCommentReq request,
		String email) {

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		RecordComment recordComment
			= recordCommentRepository.findByRecordAndId(record, recordCommentId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_COMMENT_NOT_FOUND));

		if (!Objects.equals(recordComment.getMember().getEmail(), email)) {
			throw new RecordException(FAIL_TO_UPDATE_TRAVEL_COMMENT);
		}

		recordComment.update(request.getContent());
		return RecordCommentRes.fromEntity(recordComment);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
