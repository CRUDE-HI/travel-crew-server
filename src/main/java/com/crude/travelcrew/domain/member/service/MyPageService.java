package com.crude.travelcrew.domain.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.member.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;

	@Transactional
	public Long updateNick(Long id, UpdateNickReq updateNickReq) {

		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));
		member.setNickname(updateNickReq.getNickname());

		return memberRepository.save(member).getId();
	}

	@Transactional
	public Long updatePW(Long id, UpdatePWReq updatePWReq) {

		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

		if (encoder.matches(updatePWReq.getCurrentPassword(), member.getPassword())) {
			member.setPassword(encoder.encode(updatePWReq.getNewPassword()));
			return memberRepository.save(member).getId();
		} else {
			throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
		}
	}
}
