package com.crude.travelcrew.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.custom.CustomMemberRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

	Member findByEmail(String email);

	Optional<Member> findByProfileImgUrl(String profileImgUrl);

}
