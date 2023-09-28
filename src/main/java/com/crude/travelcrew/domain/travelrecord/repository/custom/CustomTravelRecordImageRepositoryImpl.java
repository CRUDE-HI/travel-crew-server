package com.crude.travelcrew.domain.travelrecord.repository.custom;

import static com.crude.travelcrew.domain.travelrecord.model.entity.QTravelRecordImage.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomTravelRecordImageRepositoryImpl implements CustomTravelRecordImageRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long deleteAllByTravelRecordId(Long travelRecordId) {
		return queryFactory.delete(travelRecordImage)
			.where(travelRecordImage.travelRecord.id.eq(travelRecordId))
			.execute();
	}
}
