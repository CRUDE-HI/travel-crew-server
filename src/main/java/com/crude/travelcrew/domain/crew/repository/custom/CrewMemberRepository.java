package com.crude.travelcrew.domain.crew.repository.custom;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.crew.model.entity.CrewMemberId;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewMemberId> {
}
