package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewReviewReq;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.HeartBeat;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.HeartBeatRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberProfileRepository;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.CrewErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewReviewService {

	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;
	private final HeartBeatRepository heartBeatRepository;
	private final MemberProfileRepository memberProfileRepository;

	// 동행이 끝난 후 리뷰 참여하지 않은 동행글 찾기
	@Transactional
	public List<CrewListRes> unreviewed(String email) {
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if(optionalMember.isPresent()) {
			Member member = optionalMember.get();
			List<Crew> list = crewRepository.findAllUnreviewed(member.getId());
			return list.stream().map(CrewListRes::getEntity).collect(Collectors.toList());
		}else {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
	}

	@Transactional
	public void review(long crewId, List<CrewReviewReq> list, String email) {
		Crew crew = new Crew();
		crew.setCrewId(crewId);
		Optional<Member> optionalRater = memberRepository.findByEmail(email);

		if(optionalRater.isPresent()) {
			Member rater = optionalRater.get();
			long raterId = rater.getId();

			// 본인에게 리뷰 불가
			for (CrewReviewReq reviewReq : list) {
				if (reviewReq.getMemberId() == raterId) {
					throw new CrewException(CrewErrorCode.FAIL_TO_REVIEW_CREW_RITER);
				}
			}

			// 한 동행에 중복 리뷰 불가
			if (heartBeatRepository.existsByCrewAndRater(crew, rater)) {
				throw new CrewException(CrewErrorCode.ALREADY_REVIEW_CREW_RITER);
			}

			List<HeartBeat> heartBeatList = list.stream()
				.map(crewReviewReq -> CrewReviewReq.toEntity(crewReviewReq, crew, rater))
				.collect(Collectors.toList());
			heartBeatRepository.saveAll(heartBeatList);

			List<Long> memberIds = list.stream().map(CrewReviewReq::getMemberId).collect(Collectors.toList());
			for (Long memberId : memberIds) {
				memberProfileRepository.updateHeartBeatByMember(memberId);
			}
		}else {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
	}

}
