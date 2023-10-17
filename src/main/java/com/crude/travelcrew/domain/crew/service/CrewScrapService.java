package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;
import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewScrap;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.CrewScrapRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewScrapService {
	private final CrewScrapRepository crewScrapRepository;
	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;

	@Transactional
	public void scrapCrew(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		if (crewScrapRepository.existsByMemberAndCrew(member, crew)) {
			throw new CrewException(ALREADY_SCRAP_CREW);
		}

		CrewScrap crewScrap = CrewScrap.builder()
			.member(member)
			.crew(crew)
			.build();

		crewScrapRepository.save(crewScrap);
	}

	@Transactional
	public void deleteScrap(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		CrewScrap scrap = crewScrapRepository.findByMemberAndCrew(member, crew)
			.orElseThrow(() -> new CrewException(CREW_SCRAP_NOT_FOUND));

		crewScrapRepository.delete(scrap);
	}
}
