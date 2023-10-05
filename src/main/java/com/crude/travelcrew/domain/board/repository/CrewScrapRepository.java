package com.crude.travelcrew.domain.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.board.entity.Crew;
import com.crude.travelcrew.domain.board.entity.CrewScrap;
import com.crude.travelcrew.domain.member.entity.Member;

public interface CrewScrapRepository extends JpaRepository<CrewScrap, Long> {
	Optional<CrewScrap> findByMemberAndCrew(Member member, Crew crew);

	boolean existsByMemberAndCrew(Member member, Crew crew);
}
