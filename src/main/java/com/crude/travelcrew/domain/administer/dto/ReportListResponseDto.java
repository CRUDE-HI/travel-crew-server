package com.crude.travelcrew.domain.administer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportListResponseDto {

	private Long id;
	private String content;
	private String reporterNickname;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
