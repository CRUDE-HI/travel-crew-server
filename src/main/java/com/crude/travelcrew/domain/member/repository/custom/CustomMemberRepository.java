package com.crude.travelcrew.domain.member.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crude.travelcrew.domain.member.model.entity.Member;

public interface CustomMemberRepository {
	Page<Member> findReportedMembersWithQueryDsl(Pageable pageable);
}
