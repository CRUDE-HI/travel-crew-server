package com.crude.travelcrew.global.error.type;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 여행 기록에 대한 에러코드 : E201 ~ E299
 */

@Getter
@AllArgsConstructor
public enum TravelRecordErrorCode {

	TRAVEL_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "E201", "여행 기록이 존재하지 않습니다."),
	FAIL_TO_DELETE_TRAVEL_RECORD(HttpStatus.UNAUTHORIZED, "E202", "여행 기록은 작성자만 삭제할 수 있습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
