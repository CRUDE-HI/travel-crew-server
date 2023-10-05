package com.crude.travelcrew.domain.member.repository.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.entity.QMember;
import com.crude.travelcrew.domain.member.entity.QMemberProfile;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomMemberRepositoryImpl implements CustomMemberRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Member> findReportedMembersWithQueryDsl(Pageable pageable) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QMember member = QMember.member;
		QMemberProfile memberProfile = QMemberProfile.memberProfile;

		JPAQuery<Member> query = queryFactory
			.selectFrom(member)
			.join(member.memberProfile, memberProfile)
			.where(memberProfile.reportCnt.gt(0))
			.orderBy(memberProfile.reportCnt.desc());

		QueryResults<Member> results = query.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}
}
