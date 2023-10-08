package com.crude.travelcrew.domain.member.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import javax.security.auth.login.LoginException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;
import com.crude.travelcrew.domain.member.model.constants.ProviderType;
import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.crude.travelcrew.domain.member.model.dto.SignUpRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;
import com.crude.travelcrew.domain.member.repository.MemberProfileRepository;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, String> redisTemplate;

	private final MemberRepository memberRepository;
	private final MemberProfileRepository memberProfileRepository;
	private final BCryptPasswordEncoder encoder;

	public Member getByCredential(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	public SignUpRes login(SignUpReq signUpReq) throws LoginException {

		Member member = getByCredential(signUpReq.getEmail());
		if (member == null)
			throw new RuntimeException("가입 정보 없음.");

		if (!encoder.matches(signUpReq.getPassword(), member.getPassword())) {
			throw new RuntimeException("비밀번호 일치하지 않음.");
		}
		SignUpRes signUpRes = new SignUpRes();
		String email = member.getEmail();
		String nickname = member.getNickname();
		String accesstoken = jwtProvider.createAccessToken(email);
		String refreshtoken = jwtProvider.createRefreshToken(email);
		signUpRes.setEmail(email);
		signUpRes.setNickname(nickname);
		signUpRes.setAccesstoken(accesstoken);
		signUpRes.setRefreshtoken(refreshtoken);
		System.out.println("email: " + email);
		redisTemplate.opsForValue()
			.set("JWT_ACCESS_TOKEN:" + email, accesstoken + "|" + jwtProvider.getExpiration(accesstoken));
		redisTemplate.opsForValue()
			.set("JWT_REFRESH_TOKEN:" + email, refreshtoken + "|" + jwtProvider.getExpiration(refreshtoken));
		return signUpRes;
	}

	public void logout() throws Exception {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if (redisTemplate.opsForValue().get("JWT_ACCESS_TOKEN:" + email) != null) {
			redisTemplate.delete("JWT_ACCESS_TOKEN:" + email); //Token 삭제
		}
	}

	public Member signUp(Member member) {
		String rawPw = member.getPassword();
		member.setPassword(encoder.encode(rawPw));
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
