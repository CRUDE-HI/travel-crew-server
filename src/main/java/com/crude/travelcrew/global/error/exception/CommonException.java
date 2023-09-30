package com.crude.travelcrew.global.error.exception;

import com.crude.travelcrew.global.error.type.CommonErrorCode;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

	private final CommonErrorCode errorCode;

	public CommonException(CommonErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
