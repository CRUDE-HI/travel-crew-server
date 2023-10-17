package com.crude.travelcrew.domain.notification.model.dto;

import com.crude.travelcrew.domain.notification.model.constants.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	private NotificationType type;

	private String nickname; // receiver

	private Long targetId;
}
