package com.crude.travelcrew.global.error.type;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공통 에러 코드 = E001 ~ E099
 */

@Getter
@AllArgsConstructor
public enum CommonErrorCode {

	INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "E001", "이미지 형식이 유효하지 않습니다."),
	FAIL_TO_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "이미지 업로드에 실패하였습니다."),
	INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "E003", "유효성 검증 실패"),
	IMAGE_SIZE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "E004", "이미지 업로드 크기를 초과하였습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
