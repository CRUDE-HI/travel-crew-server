package com.crude.travelcrew.domain.crew.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.service.CrewReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewReviewController {
	private final CrewReviewService crewReviewService;

	// 동행이 끝난 후 리뷰 작성하지 않은 동행글 찾기
	@GetMapping("/unreviewed")
	public ResponseEntity<List<CrewListRes>> unreviewed(Principal principal) {
		return ResponseEntity.ok(crewReviewService.unreviewed(principal.getName()));
	}
}
