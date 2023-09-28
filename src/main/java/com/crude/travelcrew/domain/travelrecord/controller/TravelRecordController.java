package com.crude.travelcrew.domain.travelrecord.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.EditTravelRecordRes;
import com.crude.travelcrew.domain.travelrecord.service.TravelRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class TravelRecordController {

	private final TravelRecordService travelRecordService;

	/**
	 * 여행 기록 작성
	 */
	@PostMapping
	public ResponseEntity<EditTravelRecordRes> createTravelRecord(
		@RequestPart(value = "images", required = false) List<MultipartFile> images,
		@RequestPart @Valid EditTravelRecordReq request, Principal principal) {

		EditTravelRecordRes response = travelRecordService.createTravelRecord(request, images,
			principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 여행 기록 삭제
	 */
	@DeleteMapping("/{travelRecordId}")
	public ResponseEntity<Map<String, String>> deleteTravelRecord(@PathVariable Long travelRecordId,
		Principal principal) {

		Map<String, String> response = travelRecordService.deleteTravelRecord(travelRecordId, principal.getName());
		return ResponseEntity.ok(response);
	}
}
