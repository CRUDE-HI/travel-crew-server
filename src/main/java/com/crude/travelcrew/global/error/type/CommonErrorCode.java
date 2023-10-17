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
	IMAGE_SIZE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "E004", "이미지 업로드 크기를 초과하였습니다."),
	FAIL_TO_DELETE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "E005", "이미지 삭제에 실패하였습니다."),
	FAIL_TO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "E006", "사용자 인증에 실패하였습니다."),
	FAIL_TO_AUTHORIZATION(HttpStatus.FORBIDDEN, "E007", "사용자 접근 권한이 없습니다."),
	REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "E008", "해당 신고가 존재하지 않습니다."),
	FAIL_TO_REPORT(HttpStatus.BAD_REQUEST, "E009", "자기 자신은 신고할 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
