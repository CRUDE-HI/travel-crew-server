package com.crude.travelcrew.domain.record.service;

import java.util.Map;

import com.crude.travelcrew.domain.record.model.dto.RecordCommentReq;
import com.crude.travelcrew.domain.record.model.dto.RecordCommentRes;

public interface RecordCommentService {

	/**
	 * 여행 기록 댓글 작성
	 */
	RecordCommentRes addRecordComment(Long recordId, RecordCommentReq request, String email);

	/**
	 * 여행 기록 댓글 삭제
	 */
	Map<String, String> removeRecordComment(Long recordId, Long recordCommentId, String email);

	/**
	 * 여행 기록 댓글 수정
	 */
	RecordCommentRes updateRecordComment(Long recordId, Long recordCommentId, RecordCommentReq request, String email);
}
