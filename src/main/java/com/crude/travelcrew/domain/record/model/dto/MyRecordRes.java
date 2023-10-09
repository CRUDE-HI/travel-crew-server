package com.crude.travelcrew.domain.record.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRecordRes {

	private Long recordId;

	private String memberNick;

	private String title;

	private String content;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
