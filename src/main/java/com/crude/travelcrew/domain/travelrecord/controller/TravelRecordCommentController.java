package com.crude.travelcrew.domain.travelrecord.controller;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordCommentReq;
import com.crude.travelcrew.domain.travelrecord.model.dto.TravelRecordCommentRes;
import com.crude.travelcrew.domain.travelrecord.service.TravelRecordCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class TravelRecordCommentController {

	private final TravelRecordCommentService travelRecordCommentService;

	/**
	 * 여행 기록 댓글 작성
	 */
	@PostMapping("/{travelRecordId}/comment")
	public ResponseEntity<TravelRecordCommentRes> addTravelRecordComment(@PathVariable Long travelRecordId,
		@RequestBody @Valid TravelRecordCommentReq request, Principal principal) {

		TravelRecordCommentRes response = travelRecordCommentService.addTravelRecordComment(travelRecordId, request,
			principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 여행 기록 댓글 삭제
	 */
	@DeleteMapping("/{travelRecordId}/comment/{travelRecordCommentId}")
	public ResponseEntity<Map<String, String>> removeTravelRecordComment(@PathVariable Long travelRecordId,
		@PathVariable Long travelRecordCommentId, Principal principal) {

		Map<String, String> response = travelRecordCommentService.removeTravelRecordComment(travelRecordId,
			travelRecordCommentId, principal.getName());
		return ResponseEntity.ok(response);
	}

	/**
	 * 여행 기록 댓글 수정
	 */
	@PatchMapping("/{travelRecordId}/comment/{travelRecordCommentId}")
	public ResponseEntity<TravelRecordCommentRes> updateTravelRecordComment(@PathVariable Long travelRecordId,
		@PathVariable Long travelRecordCommentId, @RequestBody @Valid TravelRecordCommentReq request,
		Principal principal) {

		TravelRecordCommentRes response = travelRecordCommentService.updateTravelRecordComment(travelRecordId,
			travelRecordCommentId, request, principal.getName());
		return ResponseEntity.ok(response);
	}
}
