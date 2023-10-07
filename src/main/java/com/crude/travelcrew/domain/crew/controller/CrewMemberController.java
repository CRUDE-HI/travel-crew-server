package com.crude.travelcrew.domain.crew.controller;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.model.dto.ApplyForCrewReq;
import com.crude.travelcrew.domain.crew.service.CrewMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewMemberController {

	private final CrewMemberService crewMemberService;

	@PostMapping("/{crewId}/apply")
	public ResponseEntity<Map<String, String>> applyForCrewMember(@PathVariable Long crewId,
		@RequestBody @Valid ApplyForCrewReq request,
		Principal principal) {

		Map<String, String> response = crewMemberService.applyForCrewMember(crewId, request, principal.getName());
		return ResponseEntity.ok(response);
	}
}
