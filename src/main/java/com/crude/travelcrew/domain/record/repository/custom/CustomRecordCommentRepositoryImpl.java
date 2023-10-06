package com.crude.travelcrew.domain.record.repository.custom;

import static com.crude.travelcrew.domain.record.model.entity.QRecordComment.*;

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
