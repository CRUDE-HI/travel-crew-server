package com.crude.travelcrew.domain.travelrecord.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordRes;
import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordImageRes;

public interface TravelRecordService {

	/**
	 * 여행 기록 작성
	 */
	EditTravelRecordRes createTravelRecord(EditTravelRecordReq request, List<MultipartFile> images, String email);

	/**
	 * 여행 기록 삭제
	 */
	Map<String, String> deleteTravelRecord(Long travelRecordId, String email);

	/**
	 * 여행 기록 수정
	 */
	EditTravelRecordRes updateTravelRecord(Long travelRecordId, EditTravelRecordReq request, String email);

	/**
	 * 여행 기록 이미지 추가
	 */
	TravelRecordImageRes addTravelRecordImage(Long travelRecordId, MultipartFile image);

	/**
	 * 여행 기록 이미지 삭제
	 */
	Map<String, String> removeTravelRecordImage(Long travelRecordId, Long travelRecordImageId);
}
