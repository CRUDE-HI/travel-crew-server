package com.crude.travelcrew.domain.crew.service;

import java.util.List;
import java.util.Map;

import com.crude.travelcrew.domain.crew.model.dto.ApplyForCrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewMemberRes;

public interface CrewMemberService {

	/**
	 * 동행 신청
	 */
	Map<String, String> applyForCrewMember(Long crewId, ApplyForCrewReq request, String email);

	/**
	 * 동행 신청 취소
	 */
	Map<String, String> cancelCrewMember(Long crewId, String email);

	/**
	 * 동행 신청한 회원 목록 조회
	 */
	List<CrewMemberRes> getCrewMemberList(Long crewId);
}
