package com.crude.travelcrew.domain.member.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.member.model.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.model.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.service.MyPageService;
import com.crude.travelcrew.domain.record.model.dto.EditRecordRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	// 닉네임 변경
	@PatchMapping("/nickname")
	public ResponseEntity<Void> updateNick(@RequestBody UpdateNickReq updateNickReq, Principal principal) {
		myPageService.updateNick(updateNickReq, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 비밀번호 변경
	@PatchMapping("/password")
	public ResponseEntity<Void> updatePW(@RequestBody UpdatePWReq updatePWReq, Principal principal) {
		myPageService.updatePW(updatePWReq, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 프로필이미지 업로드
	@PatchMapping("/profile")
	public ResponseEntity<Void> updateProfile(@RequestBody MultipartFile image, Principal principal) {
		myPageService.updateImg(image, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 내가 쓴 동행 글 조회
	@GetMapping("/post")
	public ResponseEntity<List<CrewRes>> getMyCrewList() {
		return ResponseEntity.ok(myPageService.getMyCrewList());
	}

	// 내가 쓴 여행기록 글 조회
	@GetMapping("/record")
	public ResponseEntity<List<EditRecordRes>> getMyRecordList() {
		return ResponseEntity.ok(myPageService.getMyRecordList());
	}

}
