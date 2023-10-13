package com.crude.travelcrew.domain.notification.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;
import com.crude.travelcrew.domain.notification.service.NotificationService;
import com.crude.travelcrew.global.security.CustomUserDetails;
import com.crude.travelcrew.global.util.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/notification")
	public ResponseEntity<Slice<NotificationInfoRes>> getNotificationList(@PageableDefault Pageable pageable,
		@RequestParam(required = false) Long lastId, @AuthUser CustomUserDetails userDetails) {

		Slice<NotificationInfoRes> response
			= notificationService.getNotificationList(pageable, lastId, userDetails.getUsername());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/notification/{notificationId}")
	public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable Long notificationId,
		@AuthUser CustomUserDetails userDetails) {

		Map<String, String> response = notificationService.remove(notificationId, userDetails.getUsername());
 		return ResponseEntity.ok(response);
	}

	@PatchMapping("/notification/{notificationId}")
	public ResponseEntity<Map<String, String>> readNotification(@PathVariable Long notificationId,
		@AuthUser CustomUserDetails userDetails) {

		Map<String, String> response = notificationService.read(notificationId, userDetails.getUsername());
		return ResponseEntity.ok(response);
	}
}
