package com.crude.travelcrew.domain.member.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.crew.model.constants.CrewMemberStatus;
import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.crew.model.entity.CrewScrap;
import com.crude.travelcrew.domain.crew.repository.CrewMemberRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.CrewScrapRepository;
import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;
import com.crude.travelcrew.domain.member.model.constants.ProviderType;
import com.crude.travelcrew.domain.member.model.dto.MemberRes;
import com.crude.travelcrew.domain.member.model.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.model.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.model.dto.WithDrawPW;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.record.model.dto.MyRecordRes;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.repository.RecordRepository;
import com.crude.travelcrew.global.awss3.service.AwsS3Service;
import com.crude.travelcrew.global.error.exception.MemberException;

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
	private final CrewMemberRepository crewMemberRepository;
	private final AwsS3Service awsS3Service;

	@Transactional
	// 내 정보 상세 조회
	public MemberRes myInfo(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		return member.toMemberDTO();
	}

	// 닉네임 변경
	@Transactional
	public void updateNick(UpdateNickReq updateNickReq, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		if (Objects.isNull(member)) {
			throw new IllegalArgumentException("해당 사용자를 찾을수 없습니다.");
		}

		member.setNickname(updateNickReq.getNickname());
		memberRepository.save(member);
	}

	// 비밀번호 변경
	@Transactional
	public void updatePW(UpdatePWReq updatePWReq, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

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

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		String imageUrl = awsS3Service.uploadImageFile(image, DIR);
		member.setProfileImgUrl(imageUrl);
		memberRepository.save(member);
	}

	@Transactional
	// 프로필 이미지 삭제
	public void deleteImg(String profileImgUrl, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

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

	@Transactional
	// 내가 작성한 동행글 조회
	public List<CrewRes> getMyCrewList(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		List<Crew> crewList = crewRepository.findAllByMember(member);
		return crewList.stream().map(Crew::toCrewDTO).collect(Collectors.toList());
	}

	// 내가 쓴 여행기록 글 조회
	@Transactional
	public List<MyRecordRes> getMyRecordList() {
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (Objects.isNull(email)) {
			return null;
		}
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		List<Record> recordList = recordRepository.findAllByMember(member);
		return recordList.stream().map(Record::toMyRecordDTO).collect(Collectors.toList());
	}

	@Transactional
	// 내가 스크랩한 동행글 조회
	public List<CrewRes> prtcpCrew(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		List<CrewScrap> scrapList = crewScrapRepository.findAllByMember(member);
		return scrapList
			.stream()
			.map(scraps -> scraps.getCrew().toCrewDTO())
			.collect(Collectors.toList());
	}

	// 회원 비활성화
	public void withDraw(WithDrawPW withDrawPW, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));


		if (!encoder.matches(withDrawPW.getCurrentPassword(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		} else {
			member.setMemberStatus(MemberStatus.DROP);
			member.setRole(MemberRole.DROP);
			member.setProviderType(ProviderType.DROP);

			UUID uuid = UUID.randomUUID();
			member.setEmail(Base64.getEncoder().encodeToString(uuid.toString().getBytes()));
			member.setPassword("");
			member.setNickname("탈퇴회원");
			member.setProfileImgUrl("");
			memberRepository.save(member);
		}
	}

	// 내가 신청한 동행 글 조회
	@Transactional
	public List<CrewRes> commetCrewList(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		List<CrewMember> commentCrewList = crewMemberRepository.findAllByMember(member);

		return commentCrewList
			.stream()
			.filter(crewMember -> crewMember.getStatus() != CrewMemberStatus.OWNER)
			.map(gg->gg.getCrew().toCrewDTO())
			.collect(Collectors.toList());
	}

}
