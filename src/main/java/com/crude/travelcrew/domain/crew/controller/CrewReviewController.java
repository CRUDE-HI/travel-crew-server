package com.crude.travelcrew.domain.crew.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewReviewReq;
import com.crude.travelcrew.domain.crew.service.CrewReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewReviewController {
	private final CrewReviewService crewReviewService;

	// 동행이 끝난 후 리뷰 참여하지 않은 동행글 찾기
	@GetMapping("/unreviewed")
	public ResponseEntity<List<CrewListRes>> unreviewed(Principal principal) {
		return ResponseEntity.ok(crewReviewService.unreviewed(principal.getName()));
	}

	// 리뷰 작성
	@PostMapping("/{crewId}/review")
	public ResponseEntity<Void> review(@PathVariable Long crewId, @RequestBody List<CrewReviewReq> list, Principal principal) {
		crewReviewService.review(crewId, list, principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
