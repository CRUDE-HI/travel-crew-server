package com.crude.travelcrew.global.error.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

	private int status; // http status

	private String code;

	private String message;
}
