package com.crude.travelcrew.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.model.entity.MemberProfile;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
