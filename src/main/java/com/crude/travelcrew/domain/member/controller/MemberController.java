package com.crude.travelcrew.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.member.model.dto.LoginReq;
import com.crude.travelcrew.domain.member.model.dto.LoginRes;
import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.crude.travelcrew.domain.member.model.dto.SignUpRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;
import com.crude.travelcrew.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<LoginRes> login(@RequestBody LoginReq request) {
		LoginRes response = memberService.login(request);
		return ResponseEntity.ok(response);
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
}