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

	COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "E301", "해당하는 댓글이 존재하지 않습니다."),
	FAIL_TO_MODIFY_CREW_COMMENT(HttpStatus.UNAUTHORIZED, "E302", "댓글은 작성자만 수정할 수 있습니다."),
	FAIL_TO_DELETE_CREW_COMMENT(HttpStatus.UNAUTHORIZED, "E303", "댓글은 작성자만 삭제할 수 있습니다."),
	ALREADY_APPLIED_MEMBER(HttpStatus.BAD_REQUEST, "E305", "이미 동행을 신청한 회원입니다."),
	FAIL_TO_APPLY_CREW(HttpStatus.BAD_REQUEST, "E306", "작성자 본인은 신청할 수 없습니다."),
	PROPOSAL_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "E307", "해당 동행을 신청한 회원이 없습니다."),
	FAIL_TO_CANCEL_PROPOSAL(HttpStatus.UNAUTHORIZED, "E304", "신청자 본인만 취소할 수 있습니다."),
	CREW_NOT_FOUND(HttpStatus.BAD_REQUEST, "E308", "동행 글이 존재하지 않습니다."),
	FAIL_TO_DELETE_CREW(HttpStatus.UNAUTHORIZED, "E309", "동행글 삭제는 작성자만 가능합니다. "),
	FAIL_TO_UPDATE_CREW(HttpStatus.UNAUTHORIZED, "E310", "동행글 수정은 작성자만 가능합니다. "),
	ALREADY_REVIEW_CREW_RITER(HttpStatus.BAD_REQUEST, "E311", "이미 리뷰를 참여한 동행입니다."),
	FAIL_TO_REVIEW_CREW_RITER(HttpStatus.BAD_REQUEST, "E312", "본인에게 리뷰를 남길 수 없습니다."),
	FAIL_TO_APPROVE_CREW(HttpStatus.UNAUTHORIZED, "E313", "동행 신청 승인 권한이 없습니다."),
	IMPOSSIBLE_TO_APPROVE_MEMBER(HttpStatus.BAD_REQUEST, "E314", "동행 신청 승인이 불가능합니다."),
	FAIL_TO_REJECT_CREW(HttpStatus.UNAUTHORIZED, "E315", "동행 신청 거절 권한이 없습니다."),
	IMPOSSIBLE_TO_REJECT_MEMBER(HttpStatus.BAD_REQUEST, "E316", "동행 신청 거절이 불가능합니다."),
	CREW_EXCEEDED_MAX(HttpStatus.BAD_REQUEST, "E317", "동행 인원이 가득 찼습니다."),
	ALREADY_APPROVE(HttpStatus.BAD_REQUEST, "E318", "이미 승인된 회원 입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}