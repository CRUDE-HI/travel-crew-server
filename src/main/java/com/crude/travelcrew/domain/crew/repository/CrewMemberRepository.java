package com.crude.travelcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.crew.model.entity.CrewMemberId;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewMemberId> {
	Long countByIdCrewId(Long crewId);
	boolean existsById(CrewMemberId crewMemberId);
}
