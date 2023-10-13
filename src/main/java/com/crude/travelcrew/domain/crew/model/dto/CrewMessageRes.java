package com.crude.travelcrew.domain.crew.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMessageRes {

	private String nickname;

	private String content;

	private LocalDateTime createdAt;
}
