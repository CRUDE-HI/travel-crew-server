package com.crude.travelcrew.global.error.exception;

import com.crude.travelcrew.global.error.type.CrewErrorCode;

import lombok.Getter;

@Getter
public class CrewException extends RuntimeException {
	private final CrewErrorCode errorCode;

	public CrewException(CrewErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}