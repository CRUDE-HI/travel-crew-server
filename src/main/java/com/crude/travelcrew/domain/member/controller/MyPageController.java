package com.crude.travelcrew.domain.member.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.member.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.service.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	// 닉네임 변경
	@PatchMapping("/nickname")
	public ResponseEntity<Void> updateNick(@RequestBody UpdateNickReq updateNickReq, Principal principal) {
		myPageService.updateNick(updateNickReq, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// 비밀번호 변경
	@PatchMapping("/password")
	public ResponseEntity<Void> updatePW(@RequestBody UpdatePWReq updatePWReq, Principal principal) {
		myPageService.updatePW(updatePWReq, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// 프로필이미지 업로드
	@PatchMapping("/profile")
	public ResponseEntity<Void> updateProfile(@RequestBody MultipartFile image, Principal principal) {
		myPageService.updateImg(image, principal.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
