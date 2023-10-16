package com.crude.travelcrew.domain.notification.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationInfoRes {

	private Long id;

	private String content;

	@JsonProperty("isRead")
	private boolean read;

	private String relatedUrl;

	private LocalDateTime createdAt;
}
