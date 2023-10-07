package com.crude.travelcrew.domain.crew.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.model.dto.ApplyForCrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewMemberRes;
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

	@DeleteMapping("/{crewId}/apply")
	public ResponseEntity<Map<String, String>> cancelCrewMember(@PathVariable Long crewId, Principal principal) {

		Map<String, String> response = crewMemberService.cancelCrewMember(crewId, principal.getName());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{crewId}/list")
	public ResponseEntity<List<CrewMemberRes>> getCrewMemberList(@PathVariable Long crewId) {

		List<CrewMemberRes> response = crewMemberService.getCrewMemberList(crewId);
		return ResponseEntity.ok(response);
	}
}
