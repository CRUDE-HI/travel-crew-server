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
	FAIL_TO_DELETE_TRAVEL_RECORD(HttpStatus.UNAUTHORIZED, "E202", "여행 기록은 작성자만 삭제할 수 있습니다."),
	FAIL_TO_UPDATE_TRAVEL_RECORD(HttpStatus.UNAUTHORIZED, "E203", "여행 기록은 작성자만 수정할 수 있습니다."),
	TRAVEL_RECORD_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "E204", "여행 기록 이미지가 존재하지 않습니다."),
	ALREADY_PUSH_TRAVEL_RECORD_HEART(HttpStatus.BAD_REQUEST, "E205", "여행 기록에 이미 좋아요를 눌렀습니다."),
	TRAVEL_RECORD_HEART_NOT_FOUND(HttpStatus.NOT_FOUND, "E206", "여행 기록 좋아요가 존재하지 않습니다."),
	TRAVEL_RECORD_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E207", "여행 기록 댓글이 존재하지 않습니다."),
	FAIL_TO_DELETE_TRAVEL_COMMENT(HttpStatus.UNAUTHORIZED, "E208", "여행 기록 댓글은 작성자만 삭제할 수 있습니다."),
	FAIL_TO_UPDATE_TRAVEL_COMMENT(HttpStatus.UNAUTHORIZED, "E209", "여행 기록 댓글은 작성자만 수정할 수 있습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
