package com.crude.travelcrew.domain.board.dto;

import javax.validation.constraints.NotBlank;

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
public class CrewCommentReq {
	@NotBlank(message = "댓글을 입력해주세요.")
	private String content;
}