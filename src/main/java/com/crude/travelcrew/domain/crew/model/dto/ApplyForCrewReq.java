package com.crude.travelcrew.domain.crew.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyForCrewReq {

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
}
