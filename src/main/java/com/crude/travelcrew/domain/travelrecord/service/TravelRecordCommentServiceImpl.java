package com.crude.travelcrew.domain.travelrecord.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.TravelRecordErrorCode.*;

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

		if(Objects.isNull(member)){
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
}
