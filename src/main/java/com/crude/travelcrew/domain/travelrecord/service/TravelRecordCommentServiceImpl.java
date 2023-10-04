package com.crude.travelcrew.domain.travelrecord.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.TravelRecordErrorCode.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordCommentReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordCommentRes;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordComment;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordCommentRepository;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.exception.TravelRecordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelRecordCommentServiceImpl implements TravelRecordCommentService {

	private final MemberRepository memberRepository;
	private final TravelRecordRepository travelRecordRepository;
	private final TravelRecordCommentRepository travelRecordCommentRepository;

	@Override
	@Transactional
	public TravelRecordCommentRes addTravelRecordComment(Long travelRecordId, TravelRecordCommentReq request,
		String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		TravelRecord travelRecord = travelRecordRepository.findById(travelRecordId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_NOT_FOUND));

		TravelRecordComment travelRecordComment = TravelRecordComment.builder()
			.content(request.getContent())
			.travelRecord(travelRecord)
			.member(member)
			.build();

		travelRecordCommentRepository.save(travelRecordComment);
		return TravelRecordCommentRes.fromEntity(travelRecordComment);
	}

	@Override
	@Transactional
	public Map<String, String> removeTravelRecordComment(Long travelRecordId, Long travelRecordCommentId,
		String email) {

		TravelRecord travelRecord = travelRecordRepository.findById(travelRecordId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_NOT_FOUND));

		TravelRecordComment travelRecordComment
			= travelRecordCommentRepository.findByTravelRecordAndAndId(travelRecord, travelRecordCommentId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_COMMENT_NOT_FOUND));

		if (!Objects.equals(travelRecordComment.getMember().getEmail(), email)) {
			throw new TravelRecordException(FAIL_TO_DELETE_TRAVEL_COMMENT);
		}

		travelRecordCommentRepository.deleteById(travelRecordCommentId);
		return getMessage("여행 기록 댓글이 삭제되었습니다.");
	}

	@Override
	@Transactional
	public TravelRecordCommentRes updateTravelRecordComment(Long travelRecordId, Long travelRecordCommentId,
		TravelRecordCommentReq request, String email) {

		TravelRecord travelRecord = travelRecordRepository.findById(travelRecordId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_NOT_FOUND));

		TravelRecordComment travelRecordComment
			= travelRecordCommentRepository.findByTravelRecordAndAndId(travelRecord, travelRecordCommentId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_COMMENT_NOT_FOUND));

		if (!Objects.equals(travelRecordComment.getMember().getEmail(), email)) {
			throw new TravelRecordException(FAIL_TO_UPDATE_TRAVEL_COMMENT);
		}

		travelRecordComment.update(request.getContent());
		return TravelRecordCommentRes.fromEntity(travelRecordComment);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
