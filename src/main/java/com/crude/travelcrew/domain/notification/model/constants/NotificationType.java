package com.crude.travelcrew.domain.notification.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

	NEW_PROPOSAL_ON_CREW("[동행 신청]", "새로운 동행 참가 요청이 있습니다."),
	CREW_REQUEST_APPROVED("[동행 신청 승인]","동행 참가 요청이 승인되었습니다."),
	CREW_REQUEST_REJECTED("[동행 신청 거절]","동행 참가 요청이 거부되었습니다."),
	REVIEW_REQUEST_ON_CREW( "[리뷰 요청]","종료된 동행에 대한 리뷰를 남겨주세요.");

	private final String name;
	private final String text;
}
