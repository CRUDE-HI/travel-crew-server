package com.crude.travelcrew.domain.travelrecord.repository.custom;

import static com.crude.travelcrew.domain.travelrecord.model.entity.QTravelRecordComment.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomTravelRecordCommentRepositoryImpl implements CustomTravelRecordCommentRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllByTravelRecordId(Long travelRecordId) {
		return queryFactory.delete(travelRecordComment)
			.where(travelRecordComment.travelRecord.id.eq(travelRecordId))
			.execute();
	}
}
