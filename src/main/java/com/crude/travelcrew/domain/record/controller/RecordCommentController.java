package com.crude.travelcrew.domain.record.controller;

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

import com.crude.travelcrew.domain.record.model.dto.RecordCommentReq;
import com.crude.travelcrew.domain.record.model.dto.RecordCommentRes;
import com.crude.travelcrew.domain.record.service.RecordCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordCommentController {

	private final RecordCommentService recordCommentService;

	/**
	 * 여행 기록 댓글 작성
	 */
	@PostMapping("/{recordId}/comment")
	public ResponseEntity<RecordCommentRes> addRecordComment(@PathVariable Long recordId,
		@RequestBody @Valid RecordCommentReq request, Principal principal) {

		RecordCommentRes response = recordCommentService.addRecordComment(recordId, request,
			principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 여행 기록 댓글 삭제
	 */
	@DeleteMapping("/{recordId}/comment/{recordCommentId}")
	public ResponseEntity<Map<String, String>> removeRecordComment(@PathVariable Long recordId,
		@PathVariable Long recordCommentId, Principal principal) {

		Map<String, String> response = recordCommentService.removeRecordComment(recordId, recordCommentId,
			principal.getName());
		return ResponseEntity.ok(response);
	}

	/**
	 * 여행 기록 댓글 수정
	 */
	@PatchMapping("/{recordId}/comment/{recordCommentId}")
	public ResponseEntity<RecordCommentRes> updateRecordComment(@PathVariable Long recordId,
		@PathVariable Long recordCommentId, @RequestBody @Valid RecordCommentReq request, Principal principal) {

		RecordCommentRes response = recordCommentService.updateRecordComment(recordId,
			recordCommentId, request, principal.getName());
		return ResponseEntity.ok(response);
	}
}
