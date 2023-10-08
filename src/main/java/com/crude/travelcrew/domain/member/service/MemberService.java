package com.crude.travelcrew.domain.member.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;
import com.crude.travelcrew.domain.member.model.constants.ProviderType;
import com.crude.travelcrew.domain.member.model.dto.LoginReq;
import com.crude.travelcrew.domain.member.model.dto.LoginRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;
import com.crude.travelcrew.domain.member.repository.MemberProfileRepository;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, String> redisTemplate;

	private final MemberRepository memberRepository;
	private final MemberProfileRepository memberProfileRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public Member getByCredential(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	public LoginRes login(LoginReq request) {

		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new MemberException(WRONG_MEMBER_PASSWORD);
		}

		String accessToken = jwtProvider.createAccessToken(member.getEmail());
		String refreshToken = jwtProvider.createRefreshToken(member.getEmail());

		return LoginRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public void logout() throws Exception {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if (redisTemplate.opsForValue().get("JWT_ACCESS_TOKEN:" + email) != null) {
			redisTemplate.delete("JWT_ACCESS_TOKEN:" + email); //Token 삭제
		}
	}

	public Member signUp(Member member) {
		String rawPw = member.getPassword();
		member.setPassword(passwordEncoder.encode(rawPw));
		member.setProviderType(ProviderType.DEFAULT);
		member.setRole(MemberRole.USER);
		member.setMemberStatus(MemberStatus.DEFAULT);
		return memberRepository.save(member);
	}

	public MemberProfile setProfile(MemberProfile memberReqDTO, Member member) {
		MemberProfile memberProfile = new MemberProfile();
		memberProfile.setMember(member);
		memberProfile.setName(memberReqDTO.getName());
		memberProfile.setBirth(memberReqDTO.getBirth());
		memberProfile.setGender(memberReqDTO.getGender());
		memberProfile.setReportCnt(0);
		memberProfile.setHeartBeat(60D);
		return memberProfileRepository.save(memberProfile);
	}
}
