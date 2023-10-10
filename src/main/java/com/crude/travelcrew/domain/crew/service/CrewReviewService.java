package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewReviewService {

	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;

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


}
