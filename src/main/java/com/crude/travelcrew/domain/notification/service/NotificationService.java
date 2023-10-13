package com.crude.travelcrew.domain.notification.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;

public interface NotificationService {

	/**
	 * 나의 알림 목록 조회
	 */
	Slice<NotificationInfoRes> getNotificationList(Pageable pageable, Long lastId, String email);

	/**
	 * 알림 삭제
	 */
	Map<String, String> remove(Long notificationId, String email);
}
