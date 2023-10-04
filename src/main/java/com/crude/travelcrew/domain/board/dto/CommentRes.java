package com.crude.travelcrew.domain.board.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.board.entity.Comment;

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
public class CommentRes {
	private long commentId;
	private long crewId;
	private long memberId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static CommentRes fromEntity(Comment comment) {
		return CommentRes.builder()
			.commentId(comment.getCommentId())
			.crewId(comment.getCrewId())
			.memberId(comment.getMemberId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt())
			.build();
	}
}