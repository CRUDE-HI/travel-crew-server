package com.crude.travelcrew.domain.member.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.member.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.service.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
	private final MyPageService myPageService;

	// 회원 정보수정
	@PatchMapping("/nickname/{id}")
	public Long updateNick(@PathVariable("id") Long id, @RequestBody UpdateNickReq updateNickReq) throws Exception {

		return myPageService.updateNick(id, updateNickReq);
	}

	@PatchMapping("/password/{id}")
	public Long updatePW(@PathVariable("id") Long id, @RequestBody UpdatePWReq updatePWReq) throws Exception {

		return myPageService.updatePW(id, updatePWReq);
	}
}
