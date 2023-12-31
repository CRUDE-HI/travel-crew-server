package com.crude.travelcrew.domain.record.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordCommentReq {

	@NotBlank(message = "댓글을 작성해주세요.")
	private String content;
}
