package com.crude.travelcrew.domain.member.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;
import com.crude.travelcrew.domain.member.model.constants.ProviderType;
import com.crude.travelcrew.domain.member.model.dto.LoginReq;
import com.crude.travelcrew.domain.member.model.dto.LoginRes;
import com.crude.travelcrew.domain.member.model.dto.MemberRes;
import com.crude.travelcrew.domain.member.model.dto.ReissueRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;
import com.crude.travelcrew.domain.member.repository.MemberProfileRepository;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.security.jwt.JwtProvider;
import com.crude.travelcrew.global.security.jwt.model.BlockAccessToken;
import com.crude.travelcrew.global.security.jwt.model.RefreshToken;
import com.crude.travelcrew.global.security.jwt.repository.BlockAccessTokenRepository;
import com.crude.travelcrew.global.security.jwt.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	private final MemberProfileRepository memberProfileRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final BlockAccessTokenRepository blockAccessTokenRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public Member getByCredential(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	public LoginRes login(LoginReq request) {

		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new MemberException(WRONG_MEMBER_PASSWORD);
		}

		String accessToken = jwtProvider.createAccessToken(member.getEmail());
		String refreshToken = jwtProvider.createRefreshToken(member.getEmail());

		return LoginRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public void logout(String request) {

		String accessToken = request.substring(7);
		String email = jwtProvider.getEmail(accessToken);

		BlockAccessToken blockAccessToken = BlockAccessToken.builder()
			.id(accessToken)
			.email(email)
			.expiration(jwtProvider.getRemainingTime(accessToken))
			.build();

		refreshTokenRepository.deleteById(email);
		blockAccessTokenRepository.save(blockAccessToken);
	}

	public ReissueRes reissueAccessToken(String refreshToken) {

		String email = jwtProvider.getEmail(refreshToken);

		RefreshToken refreshTokenInRedis
			= refreshTokenRepository.findById(email)
			.orElseThrow(() -> new MemberException(FAIL_TO_REISSUE_TOKEN));

		if (!Objects.equals(refreshTokenInRedis.getToken(), refreshToken)) {
			log.error("rtk in redis and rtk in request header are not equal");
			throw new MemberException(FAIL_TO_REISSUE_TOKEN);
		}

		String newAccessToken = jwtProvider.createRefreshToken(email);
		return ReissueRes.builder()
			.accessToken(newAccessToken)
			.build();
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

	public MemberRes opInfo(String nickname) {
		Optional<Member> optionalMember = memberRepository.findByNickname(nickname);

		return optionalMember.map(Member::toMemberDTO)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}
}
