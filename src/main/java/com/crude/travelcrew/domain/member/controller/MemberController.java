package com.crude.travelcrew.domain.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.member.model.dto.LoginReq;
import com.crude.travelcrew.domain.member.model.dto.LoginRes;
import com.crude.travelcrew.domain.member.model.dto.ProfileRes;
import com.crude.travelcrew.domain.member.model.dto.ReissueRes;
import com.crude.travelcrew.domain.member.model.dto.MemberRes;

import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.crude.travelcrew.domain.member.model.dto.SignUpRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;
import com.crude.travelcrew.domain.member.service.MemberService;
import com.crude.travelcrew.global.security.CustomUserDetails;
import com.crude.travelcrew.global.util.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/duplicate/email/{email}")
	public ResponseEntity<Map<String, String>> checkDuplicatedEmail(@PathVariable String email) {

		Map<String, String> response = memberService.checkDuplicatedEmail(email);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/duplicate/nickname/{nickname}")
	public ResponseEntity<Map<String, String>> checkDuplicatedNickname(@PathVariable String nickname) {

		Map<String, String> response = memberService.checkDuplicatedNickname(nickname);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginRes> login(@RequestBody LoginReq request) {
		LoginRes response = memberService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String request) {
		memberService.logout(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/token")
	public ResponseEntity<ReissueRes> reissue(@RequestHeader("REFRESH-TOKEN") String refreshToken) {
		ReissueRes response = memberService.reissueAccessToken(refreshToken);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq) {
		try {
			Member memberEntity = new Member(signUpReq);
			MemberProfile memberProfileEntity = new MemberProfile(signUpReq);
			Member member = memberService.signUp(memberEntity);

			memberService.setProfile(memberProfileEntity,
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

	@GetMapping("/profile")
	public ResponseEntity<ProfileRes> getMyProfile(@AuthUser CustomUserDetails userDetails) {

		ProfileRes response = memberService.getMyProfile(userDetails.getMember());
		return ResponseEntity.ok(response);
	}
}