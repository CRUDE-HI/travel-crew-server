package com.crude.travelcrew.domain.travelrecord.repository.custom;

import static com.crude.travelcrew.domain.travelrecord.model.entity.QRecordComment.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomRecordCommentRepositoryImpl implements CustomRecordCommentRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllByRecordId(Long recordId) {
		return queryFactory.delete(recordComment)
			.where(recordComment.record.id.eq(recordId))
			.execute();
	}
}
