package com.crude.travelcrew.domain.crew.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewReviewService {
	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;

	public List<CrewListRes> unreviewed(String email) {
		Member member = memberRepository.findByEmail(email);
		List<Crew> list = crewRepository.findAllUnreviewed(member.getId());
		return list.stream().map(CrewListRes::getEntity).collect(Collectors.toList());
	}
}
