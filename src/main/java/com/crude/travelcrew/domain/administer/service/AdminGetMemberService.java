package com.crude.travelcrew.domain.administer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.administer.dto.GetMemberRes;
import com.crude.travelcrew.domain.administer.dto.MemberListReq;
import com.crude.travelcrew.domain.administer.dto.MemberListRes;
import com.crude.travelcrew.domain.administer.dto.MemberListResponseDto;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.entity.MemberProfile;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

@Service
public class AdminGetMemberService {

	@Autowired
	MemberRepository memberRepository;

	public List<MemberListResponseDto> convertToDto(List<Member> members) {
		return members.stream()
			.map(
				member -> {
					MemberProfile profile = member.getMemberProfile();
					return new MemberListResponseDto(
						member.getId(),
						member.getEmail(),
						member.getNickname(),
						member.getMemberStatus(),
						member.getRole(),
						profile.getHeartBeat(),
						profile.getReportCnt(),
						member.getCreatedAt(),
						member.getUpdatedAt()
					);
				}
			).collect(Collectors.toList());
	}

	public MemberListRes getList(MemberListReq memberListReq) {

		Page<Member> page = memberRepository.findAll(memberListReq.pageable());

		MemberListRes memberListRes = new MemberListRes(convertToDto(page.getContent()), (int)page.getTotalElements(),
			memberListReq.getPage());

		return memberListRes;
	}

	public GetMemberRes getMember(Long memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

		MemberProfile profile = member.getMemberProfile();

		return new GetMemberRes(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getProfileImgUrl(),
			member.getMemberStatus(),
			member.getProviderType(),
			member.getRole(),
			profile.getBirth(),
			profile.getName(),
			profile.getGender(),
			profile.getHeartBeat(),
			profile.getReportCnt(),
			member.getCreatedAt(),
			member.getUpdatedAt()
		);
	}
}
