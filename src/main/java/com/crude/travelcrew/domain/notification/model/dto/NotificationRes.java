package com.crude.travelcrew.domain.notification.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.notification.model.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRes {

	private Long notificationId;

	private String type;

	private String content;

	private LocalDateTime createdAt;

	public static NotificationRes fromEntity(Notification notification) {
		return NotificationRes.builder()
			.notificationId(notification.getId())
			.type(notification.getNotificationType().name())
			.content(notification.getContent())
			.createdAt(notification.getCreatedAt())
			.build();
	}
}