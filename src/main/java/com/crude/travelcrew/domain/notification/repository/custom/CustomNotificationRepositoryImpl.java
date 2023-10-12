package com.crude.travelcrew.domain.notification.repository.custom;

import static com.crude.travelcrew.domain.member.model.entity.QMember.*;
import static com.crude.travelcrew.domain.notification.model.entity.QNotification.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<NotificationInfoRes> findByEmail(Pageable pageable, Long lastId, String email) {
		List<NotificationInfoRes> notifications = queryFactory.select(
				fields(NotificationInfoRes.class,
					notification.id.as("id"),
					notification.isRead.as("read"),
					notification.content.as("content"),
					notification.relatedUrl.as("relatedUrl"),
					notification.createdAt.as("createdAt")
				))
			.from(notification)
			.leftJoin(notification.member, member)
			.where(member.email.eq(email), lastNotificationId(lastId))
			.orderBy(notification.createdAt.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return checkLastPage(pageable, notifications);
	}

	private BooleanExpression lastNotificationId(Long lastId) {
		if (lastId == null) {
			return null;
		}
		return notification.id.lt(lastId);
	}

	private Slice<NotificationInfoRes> checkLastPage(Pageable pageable, List<NotificationInfoRes> notifications) {
		boolean hasNext = false;

		if (notifications.size() > pageable.getPageSize()) {
			hasNext = true;
			notifications.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(notifications, pageable, hasNext);
	}
}
