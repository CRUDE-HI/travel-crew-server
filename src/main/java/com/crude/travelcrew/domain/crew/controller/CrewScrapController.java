package com.crude.travelcrew.domain.crew.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.service.CrewScrapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/crew")
@RequiredArgsConstructor
public class CrewScrapController {

	private final CrewScrapService crewScrapService;

	@PostMapping("/{crewId}/scrap")
	public ResponseEntity<Void> scrapCrew(@PathVariable Long crewId, Principal principal) {
		crewScrapService.scrapCrew(crewId, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{crewId}/scrap")
	public ResponseEntity<Void> deleteScrap(@PathVariable Long crewId, Principal principal) {
		crewScrapService.deleteScrap(crewId, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
