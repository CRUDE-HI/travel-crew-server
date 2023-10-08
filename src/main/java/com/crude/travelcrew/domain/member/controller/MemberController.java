package com.crude.travelcrew.domain.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.member.model.dto.MemberRes;
import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.crude.travelcrew.domain.member.model.dto.SignUpRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;
import com.crude.travelcrew.domain.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/member")
@CrossOrigin
@Slf4j
public class MemberController {

	@Autowired
	MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<SignUpRes> login(@RequestBody SignUpReq signUpReq) throws Exception {
		System.out.println("login: " + signUpReq);
		return ResponseEntity.ok(memberService.login(signUpReq));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() throws Exception {
		System.out.println("logout");
		memberService.logout();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/test")
	public ResponseEntity<String> test() throws Exception {
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok().body(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq) throws Exception {
		try {
			Member memberEntity = new Member(signUpReq);
			MemberProfile memberProfileEntity = new MemberProfile(signUpReq);
			log.info("/api/member/sign-up request... userInfo : {}", memberEntity);
			Member member = memberService.signUp(memberEntity);

			MemberProfile memberProfile = memberService.setProfile(memberProfileEntity,
				memberService.getByCredential(member.getEmail()));
			return ResponseEntity.ok().body(new SignUpRes(member));
		} catch (Exception e) {
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 다른 회원 정보 조회
	@GetMapping("/info/{nickname}")
	public ResponseEntity<MemberRes> opInfo(@PathVariable String nickname) {
		return ResponseEntity.ok(memberService.opInfo(nickname));
	}
}