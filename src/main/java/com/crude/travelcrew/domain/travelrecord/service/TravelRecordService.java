package com.crude.travelcrew.domain.travelrecord.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordRes;

public interface TravelRecordService {

	/**
	 * 여행 기록 작성
	 */
	EditTravelRecordRes createTravelRecord(EditTravelRecordReq request, List<MultipartFile> images, String email);
}
