package com.crude.travelcrew.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.notification.model.entity.Notification;
import com.crude.travelcrew.domain.notification.repository.custom.CustomNotificationRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CustomNotificationRepository {
}
