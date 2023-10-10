package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.domain.crew.model.constants.CrewMemberStatus.*;
import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;
import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.dto.ApplyForCrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewMemberRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.crew.repository.CrewMemberRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewMemberServiceImpl implements CrewMemberService {

	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;
	private final CrewMemberRepository crewMemberRepository;

	@Override
	@Transactional
	public Map<String, String> applyForCrewMember(Long crewId, ApplyForCrewReq request, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 자신이 작성한 동행에 신청 불가
		if (Objects.equals(crew.getMember().getEmail(), member.getEmail())) {
			throw new CrewException(FAIL_TO_APPLY_CREW);
		}

		// 해당 동행을 이미 신청한 경우 중복 불가
		if (crewMemberRepository.existsByCrewAndMember(crew, member)) {
			throw new CrewException(ALREADY_APPLIED_MEMBER);
		}

		CrewMember crewMember = CrewMember.builder()
			.crew(crew)
			.member(member)
			.content(request.getContent())
			.status(WAITING)
			.build();

		crewMemberRepository.save(crewMember);
		// 추후 알림 전송 예정
		return getMessage(String.format("%s님 신청이 완료되었습니다.", member.getNickname()));
	}

	@Override
	public Map<String, String> cancelCrewMember(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		CrewMember crewMember = crewMemberRepository.findByCrewAndMember(crew, member)
			.orElseThrow(() -> new CrewException(CREW_MEMBER_NOT_FOUND));

		// 신청자가 아니면 취소할 수 없음
		if (!Objects.equals(crewMember.getMember().getEmail(), email)) {
			throw new CrewException(FAIL_TO_CANCEL_CREW_MEMBER);
		}

		crewMemberRepository.delete(crewMember);
		return getMessage(String.format("%s님 신청이 취소되었습니다.", member.getNickname()));
	}

	@Override
	public List<CrewMemberRes> getCrewMemberList(Long crewId) {

		if (!crewRepository.existsById(crewId)) {
			throw new CrewException(CREW_NOT_FOUND);
		}

		return crewMemberRepository.findAllCrewMemberByCrewId(crewId);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}

	// 나를 제외한 동행 인원 리스트
	@Transactional
	public List<Object> crewMemberList(long crewId, String email) {
		Crew crew = crewRepository.findById(crewId).orElseThrow();
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if(optionalMember.isPresent()) {
			Member member = optionalMember.get();
			List<CrewMember> list = crewMemberRepository.findAllByCrewAndMemberNotAndStatus(crew, member, APPROVED);
			return list.stream().map(CrewMemberRes::fromEntity).collect(Collectors.toList());
		}else {
			throw new MemberException(MEMBER_NOT_FOUND);
		}

	}
}
