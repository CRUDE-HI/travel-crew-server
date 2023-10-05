package com.crude.travelcrew.domain.travelrecord.repository.custom;

import static com.crude.travelcrew.domain.travelrecord.model.entity.QRecordImage.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomRecordImageRepositoryImpl implements CustomRecordImageRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllByRecordId(Long recordId) {
		return queryFactory.delete(recordImage)
			.where(recordImage.record.id.eq(recordId))
			.execute();
	}
}
