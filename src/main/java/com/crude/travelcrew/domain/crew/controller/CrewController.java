package com.crude.travelcrew.domain.crew.controller;

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

import com.crude.travelcrew.domain.crew.model.dto.CrewCommentReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewCommentRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.crew.service.CrewService;

@RestController
@RequestMapping("/api/crew")
public class CrewController {

	private final CrewService crewService;

	public CrewController(CrewService crewService) {
		this.crewService = crewService;
	}

	// 글 조회
	@GetMapping
	public ResponseEntity<List<CrewListRes>> getAllCrewList(
		@RequestParam(value = "keyword", defaultValue = "") String keyword,
		Pageable pageable) {
		return ResponseEntity.ok(crewService.getCrewList(keyword, pageable));
	}

	// 글 등록
	@PostMapping
	public ResponseEntity<CrewRes> createCrew(
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart @Valid CrewReq request, Principal principal) {

		CrewRes response = crewService.createCrew(request, image, principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 글 수정
	@PatchMapping(value = "/{crewId}")
	public ResponseEntity<CrewRes> updateCrew(
		@PathVariable Long crewId,
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestBody @Valid CrewReq request,
		Principal principal) {

		CrewRes response = crewService.updateCrew(crewId, request, principal.getName());
		return ResponseEntity.ok(response);
	}

	// 글 삭제
	@DeleteMapping("/{crewId}")
	public ResponseEntity<Map<String, String>> deleteRecord(@PathVariable Long crewId, Principal principal) {

		Map<String, String> response = crewService.deleteCrew(crewId, principal.getName());
		return ResponseEntity.ok(response);
	}

	// 댓글 조회
	@GetMapping("/{crewId}/comment")
	public ResponseEntity<List<CrewCommentRes>> getCommentList(@PathVariable long crewId, Pageable pageable) {
		return ResponseEntity.ok(crewService.getCommentList(crewId, pageable));
	}

	// 댓글 등록
	@PostMapping("/{crewId}/comment")
	public ResponseEntity<Object> createComment(@PathVariable long crewId,
		@RequestBody @Valid CrewCommentReq commentReq) {
		crewService.createComment(crewId, commentReq);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// 댓글 수정
	@PatchMapping("/{crewId}/comment/{commentId}")
	public ResponseEntity<Object> modifyComment(@PathVariable long commentId,
		@RequestBody @Valid CrewCommentReq commentReq) {
		crewService.modifyComment(commentId, commentReq);
		return ResponseEntity.ok().build();
	}

	// 댓글 삭제
	@DeleteMapping("/{crewId}/comment/{commentId}")
	public ResponseEntity<Object> deleteComment(@PathVariable long commentId) {
		crewService.deleteComment(commentId);
		return ResponseEntity.ok().build();
	}

}
