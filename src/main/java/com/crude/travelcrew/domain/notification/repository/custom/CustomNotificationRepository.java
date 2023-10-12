package com.crude.travelcrew.domain.notification.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;

public interface CustomNotificationRepository {

	Slice<NotificationInfoRes> findByEmail(Pageable pageable, Long lastId, String email);
}
