package com.crude.travelcrew.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByEmail(String email);
}
