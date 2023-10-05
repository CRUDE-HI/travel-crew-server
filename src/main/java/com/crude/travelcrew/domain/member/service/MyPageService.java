package com.crude.travelcrew.domain.member.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.global.awss3.service.AwsS3Service;
import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.model.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {
	private final static String DIR = "profile";

	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;
	private final BCryptPasswordEncoder encoder;
	private final AwsS3Service awsS3Service;

	// 닉네임 변경
	@Transactional
	public void updateNick(UpdateNickReq updateNickReq, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("해당 사용자를 찾을수 없습니다.");
		}

		member.setNickname(updateNickReq.getNickname());
		memberRepository.save(member);
	}

	// 비밀번호 변경
	@Transactional
	public void updatePW(UpdatePWReq updatePWReq, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("해당 사용자를 찾을수 없습니다.");
		}

		if (!encoder.matches(updatePWReq.getCurrentPassword(), member.getPassword())) {
			throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
		} else {
			if (!updatePWReq.getNewPassword().equals(updatePWReq.getValidPassword())) {
				throw new IllegalArgumentException("새로운 비밀번호가 일치하지 않습니다.");
			} else {
				member.setPassword(encoder.encode(updatePWReq.getNewPassword()));
				memberRepository.save(member);
			}
		}
	}

	// 프로필 이미지 업로드
	@Transactional
	public void updateImg(MultipartFile image, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("해당 사용자를 찾을수 없습니다.");
		}

		String imageUrl = awsS3Service.uploadImageFile(image, DIR);
		member.setProfileImgUrl(imageUrl);
		memberRepository.save(member);
	}

	// 내가 쓴 동행 글 조회
	public List<CrewRes> getMyCrewList() {
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (Objects.isNull(email)) {
			return null;
		}
		Member member = memberRepository.findByEmail(email);
		List<Crew> crewList = crewRepository.findAllByMember(member);
		return crewList.stream().map(Crew::toCrewDTO).collect(Collectors.toList());
	}

}
