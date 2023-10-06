package com.crude.travelcrew.domain.crew.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewScrap;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface CrewScrapRepository extends JpaRepository<CrewScrap, Long> {
	Optional<CrewScrap> findByMemberAndCrew(Member member, Crew crew);

	boolean existsByMemberAndCrew(Member member, Crew crew);
}
