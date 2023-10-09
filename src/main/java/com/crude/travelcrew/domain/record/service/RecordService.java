package com.crude.travelcrew.domain.record.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.record.model.dto.EditRecordReq;
import com.crude.travelcrew.domain.record.model.dto.EditRecordRes;
import com.crude.travelcrew.domain.record.model.dto.GetRecordRes;
import com.crude.travelcrew.domain.record.model.dto.RecordImageRes;

public interface RecordService {

	/**
	 * 여행 기록 상세 조회
	 */
	GetRecordRes getRecord(Long recordId);

	/**
	 * 여행 기록 작성
	 */
	EditRecordRes addRecord(EditRecordReq request, List<MultipartFile> images, String email);

	/**
	 * 여행 기록 삭제
	 */
	Map<String, String> deleteRecord(Long recordId, String email);

	/**
	 * 여행 기록 수정
	 */
	EditRecordRes updateRecord(Long recordId, EditRecordReq request, String email);

	/**
	 * 여행 기록 이미지 추가
	 */
	RecordImageRes addRecordImage(Long recordId, MultipartFile image);

	/**
	 * 여행 기록 이미지 삭제
	 */
	Map<String, String> removeRecordImage(Long recordId, Long recordImageId);
}
