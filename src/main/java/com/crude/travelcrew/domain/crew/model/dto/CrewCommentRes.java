package com.crude.travelcrew.domain.crew.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.crew.model.entity.CrewComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CrewCommentRes {
	private long commentId;
	private long crewId;
	private long memberId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static CrewCommentRes fromEntity(CrewComment crewComment) {
		return CrewCommentRes.builder()
			.commentId(crewComment.getCommentId())
			.crewId(crewComment.getCrewId())
			.memberId(crewComment.getMemberId())
			.content(crewComment.getContent())
			.createdAt(crewComment.getCreatedAt())
			.updatedAt(crewComment.getUpdatedAt())
			.build();
	}
}