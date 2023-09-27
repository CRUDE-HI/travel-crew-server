package com.crude.travelcrew.global.error.type;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원에 대한 에러 코드 = E101 ~ E199
 */

@Getter
@AllArgsConstructor
public enum MemberErrorCode {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "E101", "회원이 존재하지 않습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
