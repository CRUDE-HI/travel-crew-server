package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.domain.crew.model.constants.ProposalStatus.*;
import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;
import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.dto.AddProposalReq;
import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.Proposal;
import com.crude.travelcrew.domain.crew.repository.ProposalRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

	private final MemberRepository memberRepository;
	private final CrewRepository crewRepository;
	private final ProposalRepository proposalRepository;

	@Override
	@Transactional
	public Map<String, String> addProposal(Long crewId, AddProposalReq request, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 자신이 작성한 동행에 신청 불가
		if (Objects.equals(crew.getMember().getEmail(), member.getEmail())) {
			throw new CrewException(FAIL_TO_APPLY_CREW);
		}

		// 해당 동행을 이미 신청한 경우 중복 불가
		if (proposalRepository.existsByCrewAndMember(crew, member)) {
			throw new CrewException(ALREADY_APPLIED_MEMBER);
		}

		Proposal proposal = Proposal.builder()
			.crew(crew)
			.member(member)
			.content(request.getContent())
			.status(WAITING)
			.build();

		proposalRepository.save(proposal);
		// 추후 알림 전송 예정
		return getMessage(String.format("%s님 신청이 완료되었습니다.", member.getNickname()));
	}

	@Override
	public Map<String, String> cancelProposal(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		Proposal proposal = proposalRepository.findByCrewAndMember(crew, member)
			.orElseThrow(() -> new CrewException(CREW_MEMBER_NOT_FOUND));

		// 신청자가 아니면 취소할 수 없음
		if (!Objects.equals(proposal.getMember().getEmail(), email)) {
			throw new CrewException(FAIL_TO_CANCEL_CREW_MEMBER);
		}

		proposalRepository.delete(proposal);
		return getMessage(String.format("%s님 신청이 취소되었습니다.", member.getNickname()));
	}

	@Override
	public List<ProposalRes> getProposalList(Long crewId) {

		if (!crewRepository.existsById(crewId)) {
			throw new CrewException(CREW_NOT_FOUND);
		}

		return proposalRepository.findAllByCrewId(crewId);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}


/* 수정 필요
	// 나를 제외한 동행 참여 인원 리스트
	@Transactional
	public List<Object> crewMemberList(long crewId, String email) {
		Crew crew = crewRepository.findById(crewId).orElseThrow();
		Optional<Member> optionalMember = memberRepository.findByEmail(email);
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			List<CrewMember> list = proposalRepository.findAllByCrewAndMemberNotAndStatus(crew, member, APPROVED);
			return list.stream().map(CrewMemberRes::fromEntity).collect(Collectors.toList());
		} else {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
	}
 */
}
