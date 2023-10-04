package com.crude.travelcrew.domain.travelrecord.service;

public interface TravelRecordHeartService {

	/**
	 * 여행 기록 좋아요 등록
	 */
	void pushTravelRecordHeart(Long travelRecordId, String email);

	/**
	 * 여행 기록 좋아요 취소
	 */
	void cancelTravelRecordHeart(Long travelRecordId, String email);
}
