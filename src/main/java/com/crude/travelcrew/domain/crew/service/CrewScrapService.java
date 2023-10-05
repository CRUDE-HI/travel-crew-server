package com.crude.travelcrew.domain.crew.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewScrap;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.CrewScrapRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewScrapService {
	private final CrewScrapRepository crewScrapRepository;
	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;

	@Transactional
	public void scrapCrew(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("사용자를 찾을수 없습니다.");
		}

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new IllegalArgumentException("게시물을 찾을수 없습니다."));

		if (crewScrapRepository.existsByMemberAndCrew(member, crew)) {
			throw new IllegalArgumentException("이미 스크랩하셨습니다.");
		}

		CrewScrap scrap = CrewScrap.builder()
			.member(member)
			.crew(crew)
			.build();

		crewScrapRepository.save(scrap);
	}

	@Transactional
	public void deleteScrap(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("사용자를 찾을수 없습니다.");
		}

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new IllegalArgumentException("게시물을 찾을수 없습니다."));

		CrewScrap scrap = crewScrapRepository.findByMemberAndCrew(member, crew)
			.orElseThrow(() -> new IllegalArgumentException("스크랩 되어있지 않습니다."));

		crewScrapRepository.delete(scrap);
	}
}
