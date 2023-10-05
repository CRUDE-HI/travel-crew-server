package com.crude.travelcrew.domain.board.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.board.service.ScrapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/crew")
@RequiredArgsConstructor
public class ScrapController {

	private final ScrapService scrapService;

	@PostMapping("/{crewId}/scrap")
	public ResponseEntity<Void> scrapPost(@PathVariable Long crewId, Principal principal) {
		scrapService.scrapPost(crewId, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{crewId}/scrap")
	public ResponseEntity<Void> deleteScrapPost(@PathVariable Long crewId, Principal principal) {
		scrapService.deleteScrapPost(crewId, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
