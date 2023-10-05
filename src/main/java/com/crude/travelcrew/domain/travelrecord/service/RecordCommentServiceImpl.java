package com.crude.travelcrew.domain.travelrecord.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.RecordErrorCode.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.travelrecord.model.dto.RecordCommentReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.RecordCommentRes;
import com.crude.travelcrew.domain.travelrecord.model.entity.Record;
import com.crude.travelcrew.domain.travelrecord.model.entity.RecordComment;
import com.crude.travelcrew.domain.travelrecord.repository.RecordCommentRepository;
import com.crude.travelcrew.domain.travelrecord.repository.RecordRepository;
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
			= recordCommentRepository.findByRecordAndAndId(record, recordCommentId)
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
			= recordCommentRepository.findByRecordAndAndId(record, recordCommentId)
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
