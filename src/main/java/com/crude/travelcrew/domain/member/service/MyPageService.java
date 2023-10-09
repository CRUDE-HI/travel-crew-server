package com.crude.travelcrew.domain.member.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewScrap;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.CrewScrapRepository;
import com.crude.travelcrew.domain.member.model.dto.MemberRes;
import com.crude.travelcrew.domain.member.model.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.model.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.record.model.dto.EditRecordRes;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.repository.RecordRepository;
import com.crude.travelcrew.global.awss3.service.AwsS3Service;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {
	private final static String DIR = "profile";

	private final MemberRepository memberRepository;
	private final RecordRepository recordRepository;
	private final CrewRepository crewRepository;
	private final CrewScrapRepository crewScrapRepository;
	private final BCryptPasswordEncoder encoder;
	private final AwsS3Service awsS3Service;

	// 내 정보 상세 조회
	public MemberRes myInfo(String email) {
		Member member = memberRepository.findByEmail(email);
		if (Objects.isNull(member)) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		return member.toMemberDTO();
	}

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

	// 프로필 이미지 삭제
	public void deleteImg(String profileImgUrl, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("해당 사용자를 찾을수 없습니다.");
		}

		memberRepository.findByProfileImgUrl(member.getProfileImgUrl())
			.orElseThrow(() -> new IllegalArgumentException("이미지 파일이 존재하지 않습니다."));

		try {
			awsS3Service.deleteImageFile(member.getProfileImgUrl(), DIR);
			member.setProfileImgUrl("삭제성공!");
			memberRepository.save(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 내가 작성한 동행글 조회
	public List<CrewRes> getMyCrewList(String email) {
		Member member = memberRepository.findByEmail(email);
		if (Objects.isNull(member)) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		List<Crew> crewList = crewRepository.findAllByMember(member);
		return crewList.stream().map(Crew::toCrewDTO).collect(Collectors.toList());
	}

	// 내가 쓴 여행기록 글 조회
	public List<EditRecordRes> getMyRecordList() {
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (Objects.isNull(email)) {
			return null;
		}
		Member member = memberRepository.findByEmail(email);
		List<Record> recordList = recordRepository.findAllByMember(member);
		return recordList.stream().map(Record::toRecordDTO).collect(Collectors.toList());
	}

	// 내가 스크랩한 동행글 조회
	public List<CrewRes> prtcpCrew(String email) {
		Member member = memberRepository.findByEmail(email);
		if (Objects.isNull(member)) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		List<CrewScrap> scrapList = crewScrapRepository.findAllByMember(member);
		return scrapList
			.stream()
			.map(scraps -> scraps.getCrew().toCrewDTO())
			.collect(Collectors.toList());
	}
}
