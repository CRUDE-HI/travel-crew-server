package com.crude.travelcrew.domain.crew.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping("/post")
	public CrewRes createCrew(@RequestBody CrewReq requestDto) {
		CrewRes response = crewService.createCrew(requestDto);
		return response;
	}

	// 글 수정
	@PutMapping("/{crewId}")
	public Long updateCrew(@PathVariable Long crewId, @RequestBody CrewReq requestDto) {
		return crewService.updateCrew(crewId, requestDto);
	}

	// 글 삭제
	@DeleteMapping("/{crewId}")
	public Long deleteCrew(@PathVariable Long crewId) {
		return crewService.deleteCrew(crewId);
	}

	// 댓글 조회
	@GetMapping("/{crewId}/comment")
	public ResponseEntity<List<CrewCommentRes>> getCommentList(@PathVariable long crewId, Pageable pageable) {
		return ResponseEntity.ok(crewService.getCommentList(crewId, pageable));
	}

	// 댓글 등록
	@PostMapping("/{crewId}/comment")
	public ResponseEntity<Object> createComment(@PathVariable long crewId, @Valid CrewCommentReq commentReq) {
		crewService.createComment(crewId, commentReq);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// 댓글 수정
	@PatchMapping("/{crewId}/comment/{commentId}")
	public ResponseEntity<Object> modifyComment(@PathVariable long commentId, @Valid CrewCommentReq commentReq) {
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
