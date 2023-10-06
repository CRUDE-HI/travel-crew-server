package com.crude.travelcrew.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.model.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByEmail(String email);

	Optional<Member> findByProfileImgUrl(String profileImgUrl);

}
