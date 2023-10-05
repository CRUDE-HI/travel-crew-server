package com.crude.travelcrew.domain.travelrecord.service;

public interface RecordHeartService {

	/**
	 * 여행 기록 좋아요 등록
	 */
	void pushRecordHeart(Long recordId, String email);

	/**
	 * 여행 기록 좋아요 취소
	 */
	void cancelRecordHeart(Long recordId, String email);
}
