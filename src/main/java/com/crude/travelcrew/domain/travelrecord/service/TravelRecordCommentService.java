package com.crude.travelcrew.domain.travelrecord.service;

import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordCommentReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordCommentRes;

public interface TravelRecordCommentService {

	/**
	 * 여행 기록 댓글 작성
	 */
	TravelRecordCommentRes addTravelRecordComment(Long travelRecordId, TravelRecordCommentReq request, String email);
}
