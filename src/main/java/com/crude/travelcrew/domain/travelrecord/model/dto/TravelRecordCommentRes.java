package com.crude.travelcrew.domain.travelrecord.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelRecordCommentRes {

	private Long id;

	private String content;

	private LocalDateTime createdAt;

	public static TravelRecordCommentRes fromEntity(TravelRecordComment travelRecordComment) {
		return TravelRecordCommentRes.builder()
			.id(travelRecordComment.getId())
			.content(travelRecordComment.getContent())
			.createdAt(travelRecordComment.getCreatedAt())
			.build();
	}
}
