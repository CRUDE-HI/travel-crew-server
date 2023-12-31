package com.crude.travelcrew.domain.record.model.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class GetRecordRes {

	private Long recordId;

	private String memberNick;

	private String profileImgUrl;

	private double heartBeat;

	private String title;

	private String content;

	private List<String> imageUrls;

	private Long heartsCount;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private boolean isAuthor;
}
