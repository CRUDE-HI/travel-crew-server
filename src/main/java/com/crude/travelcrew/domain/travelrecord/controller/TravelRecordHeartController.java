package com.crude.travelcrew.domain.travelrecord.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.travelrecord.service.TravelRecordHeartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class TravelRecordHeartController {

	private final TravelRecordHeartService travelRecordHeartService;

	@PostMapping("/{travelRecordId}/heart")
	public ResponseEntity<Void> pushTravelRecordHeart(@PathVariable Long travelRecordId, Principal principal) {
		travelRecordHeartService.pushTravelRecordHeart(travelRecordId, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
