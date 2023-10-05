package com.crude.travelcrew.global.error.type;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Crew 에러 코드 = E301 ~ E399
 */
@Getter
@AllArgsConstructor
public enum CrewErrorCode {

	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E301", "해당하는 댓글이 존재하지 않습니다."),
	FAIL_TO_MODIFY_CREW_COMMENT(HttpStatus.UNAUTHORIZED, "E302", "댓글은 작성자만 수정할 수 있습니다."),
	FAIL_TO_DELETE_CREW_COMMENT(HttpStatus.UNAUTHORIZED, "E303", "댓글은 작성자만 삭제할 수 있습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}