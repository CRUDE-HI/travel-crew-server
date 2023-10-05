package com.crude.travelcrew.domain.travelrecord.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.travelrecord.model.entity.RecordComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordCommentRes {

	private Long id;

	private String content;

	private LocalDateTime createdAt;

	public static RecordCommentRes fromEntity(RecordComment recordComment) {
		return RecordCommentRes.builder()
			.id(recordComment.getId())
			.content(recordComment.getContent())
			.createdAt(recordComment.getCreatedAt())
			.build();
	}
}
