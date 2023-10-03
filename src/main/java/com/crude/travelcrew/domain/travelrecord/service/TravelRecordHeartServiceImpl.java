package com.crude.travelcrew.domain.travelrecord.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;
import static com.crude.travelcrew.global.error.type.TravelRecordErrorCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordHeart;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordHeartRepository;
import com.crude.travelcrew.domain.travelrecord.repository.TravelRecordRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.exception.TravelRecordException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelRecordHeartServiceImpl implements TravelRecordHeartService {

	private final MemberRepository memberRepository;
	private final TravelRecordRepository travelRecordRepository;
	private final TravelRecordHeartRepository travelRecordHeartRepository;

	@Override
	public void pushTravelRecordHeart(Long travelRecordId, String email) {

		Member member = memberRepository.findByEmail(email);

		if(Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		TravelRecord travelRecord = travelRecordRepository.findById(travelRecordId)
			.orElseThrow(() -> new TravelRecordException(TRAVEL_RECORD_NOT_FOUND));

		// 이미 좋아요를 누른 경우
		if(travelRecordHeartRepository.existsByTravelRecordAndMember(travelRecord, member)) {
			throw new TravelRecordException(ALREADY_PUSH_TRAVEL_RECORD_HEART);
		}

		TravelRecordHeart travelRecordHeart = TravelRecordHeart.builder()
			.travelRecord(travelRecord)
			.member(member)
			.build();

		travelRecordHeartRepository.save(travelRecordHeart);
	}
}
