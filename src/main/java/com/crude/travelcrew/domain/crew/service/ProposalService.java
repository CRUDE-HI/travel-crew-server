package com.crude.travelcrew.domain.crew.service;

import java.util.List;
import java.util.Map;

import com.crude.travelcrew.domain.crew.model.dto.AddProposalReq;
import com.crude.travelcrew.domain.crew.model.dto.EditProposalStatusReq;
import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface ProposalService {

	/**
	 * 동행 신청
	 */
	Map<String, String> addProposal(Long crewId, AddProposalReq request, Member member);

	/**
	 * 동행 신청 취소
	 */
	Map<String, String> cancelProposal(Long crewId, Member member);

	/**
	 * 동행 신청 승인
	 */
	Map<String, String> approveProposal(Long crewId, EditProposalStatusReq request, Member member);

	/**
	 * 동행 신청 거절
	 */
	Map<String, String> rejectProposal(Long crewId, EditProposalStatusReq request, Member member);

	/**
	 * 동행 신청한 회원 목록 조회
	 */
	List<ProposalRes> getProposalList(Long crewId);

	/**
	 * 나를 제외한 동행 참여 인원 리스트
	 */
/*
	List<Object> crewMemberList(long crewId, String email);
 */
}
