package com.crude.travelcrew.domain.crew.repository.custom;

import static com.crude.travelcrew.domain.crew.model.entity.QCrewMember.*;
import static com.crude.travelcrew.domain.member.model.entity.QMember.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import com.crude.travelcrew.domain.crew.model.dto.CrewMemberRes;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCrewMemberRepositoryImpl implements CustomCrewMemberRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<CrewMemberRes> findAllCrewMemberByCrewId(Long crewId) {
		return queryFactory.select(fields(CrewMemberRes.class,
				member.profileImgUrl.as("profileImgUrl"),
				member.nickname.as("nickname"),
				crewMember.content.as("content"),
				crewMember.status.as("status")
			))
			.from(crewMember)
			.leftJoin(crewMember.member, member)
			.fetch();
	}
}
