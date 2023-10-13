package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.domain.crew.model.constants.ProposalStatus.*;
import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.dto.AddProposalReq;
import com.crude.travelcrew.domain.crew.model.dto.EditProposalStatusReq;
import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.crew.model.entity.Proposal;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.ProposalRepository;
import com.crude.travelcrew.domain.crew.repository.custom.CrewMemberRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

	private final CrewRepository crewRepository;
	private final ProposalRepository proposalRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public Map<String, String> addProposal(Long crewId, AddProposalReq request, Member member) {

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
		return getMessage(String.format("%s님 신청이 완료되었습니다.", member.getNickname()));
	}

	@Override
	@Transactional
	public Map<String, String> cancelProposal(Long crewId, Member member) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		Proposal proposal = proposalRepository.findByCrewAndMember(crew, member)
			.orElseThrow(() -> new CrewException(PROPOSAL_MEMBER_NOT_FOUND));

		proposalRepository.delete(proposal);
		return getMessage(String.format("%s님 신청이 취소되었습니다.", member.getNickname()));
	}

	@Override
	@Transactional
	public Map<String, String> approveProposal(Long crewId, EditProposalStatusReq request, String email) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 동행 작성자만 승인 가능
		if (!Objects.equals(email, crew.getMember().getEmail())) {
			throw new CrewException(FAIL_TO_APPROVE_CREW);
		}

		// 신청 승인이 가능한 상태인지 확인 (= WAITING && 최대 인원 수)
		Proposal proposal
			= proposalRepository.findPossibleToApprove(crewId, request.getNickname(), WAITING, crew.getMaxCrew())
			.orElseThrow(() -> new CrewException(IMPOSSIBLE_TO_APPROVE_MEMBER));

		proposal.approve();

		// crew member 저장
		Member proposer = memberRepository.findByNickname(request.getNickname())
			.orElseThrow(() -> new CrewException(PROPOSAL_MEMBER_NOT_FOUND));

		crewMemberRepository.save(new CrewMember(proposer.getId(), crew.getCrewId()));
		return getMessage(String.format("%s님의 동행 신청을 수락하였습니다.", request.getNickname()));
	}

	@Override
	public Map<String, String> rejectProposal(Long crewId, EditProposalStatusReq request, Member member) {

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 동행 작성자만 거절 가능
		if (!Objects.equals(member.getEmail(), crew.getMember().getEmail())) {
			throw new CrewException(FAIL_TO_REJECT_CREW);
		}

		// 신청 거절이 가능한 상태인지 확인 (= WAITING)
		Proposal proposal
			= proposalRepository.findPossibleToReject(crewId, request.getNickname(), WAITING)
			.orElseThrow(() -> new CrewException(IMPOSSIBLE_TO_REJECT_MEMBER));

		proposal.reject();
		return getMessage(String.format("%s님의 동행 신청을 거절하였습니다.", request.getNickname()));
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
