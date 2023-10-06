package com.crude.travelcrew.domain.record.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.record.service.RecordHeartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordHeartController {

	private final RecordHeartService recordHeartService;

	@PostMapping("/{travelRecordId}/heart")
	public ResponseEntity<Void> pushRecordHeart(@PathVariable Long travelRecordId, Principal principal) {
		recordHeartService.pushRecordHeart(travelRecordId, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{travelRecordId}/heart")
	public ResponseEntity<Void> cancelRecordHeart(@PathVariable Long travelRecordId, Principal principal) {

		recordHeartService.cancelRecordHeart(travelRecordId, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
