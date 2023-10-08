package com.crude.travelcrew.domain.crew.service;

import java.util.Map;

import com.crude.travelcrew.domain.crew.model.dto.ApplyForCrewReq;

public interface CrewMemberService {

	/**
	 * 동행 신청
	 */
	Map<String, String> applyForCrewMember(Long crewId, ApplyForCrewReq request, String email);

	/**
	 * 동행 신청 취소
	 */
	Map<String, String> cancelCrewMember(Long crewId, String email);
}