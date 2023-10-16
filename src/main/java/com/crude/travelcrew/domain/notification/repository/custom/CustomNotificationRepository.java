package com.crude.travelcrew.domain.notification.repository.custom;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;
import com.crude.travelcrew.domain.notification.model.entity.Notification;

public interface CustomNotificationRepository {

	Slice<NotificationInfoRes> findByEmail(Pageable pageable, Long lastId, String email);

	Optional<Notification> findByEmailAndId(String email, Long notificationId);
}
