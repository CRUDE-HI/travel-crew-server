package com.crude.travelcrew.domain.member.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.member.model.dto.LoginRes;
import com.crude.travelcrew.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/oauth2")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

	private final MemberService memberService;

	@GetMapping("/naver")
	public ResponseEntity<?> naverLogin(OAuth2AuthenticationToken authentication) {
		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		OAuth2User oAuth2User = authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// 네이버 로그인의 응답 정보들을 맵에 저장
		Map<String, Object> naverResponse = new HashMap<>();
		naverResponse.put("email", attributes.get("email"));
		naverResponse.put("name", attributes.get("name"));
		naverResponse.put("nickname", attributes.get("nickname"));
		naverResponse.put("birthday", attributes.get("birthday"));

		// MemberService에서 사용자 확인 및 등록, 토큰 발급까지의 과정을 하나의 메소드로 처리
		LoginRes loginRes = memberService.naverLogin(naverResponse);

		return ResponseEntity.ok()
			.header("Authorization", "Bearer " + loginRes.getAccessToken())
			.body(loginRes);  // 토큰 정보도 본문에 포함하여 반환
	}

	@GetMapping("/naver/callback")
	public ResponseEntity<?> naverCallback(OAuth2AuthenticationToken authentication) {
		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		OAuth2User oAuth2User = authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// 네이버 로그인의 응답 정보들을 맵에 저장
		Map<String, Object> naverResponse = new HashMap<>();
		naverResponse.put("email", attributes.get("email"));
		naverResponse.put("name", attributes.get("name"));
		naverResponse.put("nickname", attributes.get("nickname"));
		naverResponse.put("birthday", attributes.get("birthday"));

		// MemberService에서 사용자 확인 및 등록, 토큰 발급까지의 과정을 하나의 메소드로 처리
		LoginRes loginRes = memberService.naverLogin(naverResponse);

		// 리다이렉션: 메인 페이지나 다른 페이지로 사용자를 리다이렉션 할 수 있습니다.
		URI redirectUri = URI.create("/main");
		return ResponseEntity.status(HttpStatus.FOUND)
			.location(redirectUri)
			.header("Authorization", "Bearer " + loginRes.getAccessToken())
			.build();
	}
}
