package com.crude.travelcrew.domain.crew.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

	boolean existsByCrewAndMember(Crew crew, Member member);

	Optional<CrewMember> findByCrewAndMember(Crew crew, Member member);
}
