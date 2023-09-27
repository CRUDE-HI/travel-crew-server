package com.crude.travelcrew.global.error.exception;

import com.crude.travelcrew.global.error.type.TravelRecordErrorCode;

import lombok.Getter;

@Getter
public class TravelRecordException extends RuntimeException {

	private final TravelRecordErrorCode errorCode;

	public TravelRecordException(TravelRecordErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
