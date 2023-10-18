package com.crude.travelcrew.domain.record.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.record.model.dto.EditRecordReq;
import com.crude.travelcrew.domain.record.model.dto.EditRecordRes;
import com.crude.travelcrew.domain.record.model.dto.GetRecordRes;
import com.crude.travelcrew.domain.record.model.dto.RecordImageRes;
import com.crude.travelcrew.domain.record.model.dto.RecordListRes;
import com.crude.travelcrew.domain.record.service.RecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {

	private final RecordService recordService;

	/**
	 * 여행 기록 상세 조회
	 */
	@GetMapping("/{recordId}")
	public ResponseEntity<GetRecordRes> getRecord(@PathVariable Long recordId, Principal principal) {
		GetRecordRes getRecordRes = recordService.getRecord(recordId, principal.getName());
		return ResponseEntity.ok(getRecordRes);
	}

	/**
	 * 여행 기록 전체글 조회
	 */
	// 글 조회
	@GetMapping
	public ResponseEntity<Map<String, Object>> listAllCrew(
		@RequestParam(value = "keyword", defaultValue = "") String keyword,
		Pageable pageable) {
		return ResponseEntity.ok(recordService.listRecord(keyword, pageable));
	}

	/**
	 * 여행 기록 작성
	 */
	@PostMapping
	public ResponseEntity<EditRecordRes> addRecord(
		@RequestPart(value = "images", required = false) List<MultipartFile> images,
		@RequestPart @Valid EditRecordReq request, Principal principal) {

		EditRecordRes response = recordService.addRecord(request, images, principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 여행 기록 삭제
	 */
	@DeleteMapping("/{recordId}")
	public ResponseEntity<Map<String, String>> deleteRecord(@PathVariable Long recordId, Principal principal) {

		Map<String, String> response = recordService.deleteRecord(recordId, principal.getName());
		return ResponseEntity.ok(response);
	}

	/**
	 * 여행 기록 수정
	 */
	@PatchMapping("/{recordId}")
	public ResponseEntity<EditRecordRes> updateRecord(@PathVariable Long recordId,
		@RequestBody @Valid EditRecordReq request, Principal principal) {

		EditRecordRes response = recordService.updateRecord(recordId, request, principal.getName());
		return ResponseEntity.ok(response);
	}

	/**
	 * 여행 기록 이미지 추가
	 */
	@PostMapping("/{recordId}/image")
	public ResponseEntity<RecordImageRes> addRecordImage(@PathVariable Long recordId,
		@RequestPart MultipartFile image) {

		RecordImageRes response = recordService.addRecordImage(recordId, image);
		return ResponseEntity.ok(response);
	}

	/**
	 * 여행 기록 이미지 삭제
	 */
	@DeleteMapping("/{recordId}/image/{recordImageId}")
	public ResponseEntity<Map<String, String>> removeRecordImage(@PathVariable Long recordId,
		@PathVariable Long recordImageId) {

		Map<String, String> response = recordService.removeRecordImage(recordId, recordImageId);
		return ResponseEntity.ok(response);
	}
}
