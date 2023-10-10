package com.crude.travelcrew.domain.crew.repository.custom;


import static com.crude.travelcrew.domain.crew.model.entity.QCrewComment.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCrewCommentRepositoryImpl implements CustomCrewCommentRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllByCrewId(Long crewId) {
		return queryFactory.delete(crewComment)
			.where(crewComment.crewId.eq(crewId))
			.execute();
	}
}
