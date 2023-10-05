package com.crude.travelcrew.domain.record.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.RecordErrorCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordHeart;
import com.crude.travelcrew.domain.record.repository.RecordHeartRepository;
import com.crude.travelcrew.domain.record.repository.RecordRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.exception.RecordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordHeartServiceImpl implements RecordHeartService {

	private final MemberRepository memberRepository;
	private final RecordRepository recordRepository;
	private final RecordHeartRepository recordHeartRepository;

	@Override
	@Transactional
	public void pushRecordHeart(Long recordId, String email) {

		Member member = memberRepository.findByEmail(email);

		if(Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		// 이미 좋아요를 누른 경우
		if(recordHeartRepository.existsByRecordAndMember(record, member)) {
			throw new RecordException(ALREADY_PUSH_TRAVEL_RECORD_HEART);
		}

		RecordHeart recordHeart = RecordHeart.builder()
			.record(record)
			.member(member)
			.build();

		recordHeartRepository.save(recordHeart);
	}

	@Override
	@Transactional
	public void cancelRecordHeart(Long recordId, String email) {

		Member member = memberRepository.findByEmail(email);

		if(Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		Record record = recordRepository.findById(recordId)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_NOT_FOUND));

		RecordHeart recordHeart = recordHeartRepository.findByRecordAndMember(record, member)
			.orElseThrow(() -> new RecordException(TRAVEL_RECORD_HEART_NOT_FOUND));

		recordHeartRepository.delete(recordHeart);
	}
}
