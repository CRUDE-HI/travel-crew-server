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

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "E101", "회원이 존재하지 않습니다."),
	WRONG_MEMBER_PASSWORD(HttpStatus.UNAUTHORIZED, "E102", "회원 비밀번호가 일치하지 않습니다."),
	FAIL_TO_REISSUE_TOKEN(HttpStatus.UNAUTHORIZED, "E103", "회원 토큰 재발급이 불가능합니다."),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "E104", "이미 존재하는 이메일입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "E105", "이미 존재하는 닉네임입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
