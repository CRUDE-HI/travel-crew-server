package com.crude.travelcrew.domain.crew.repository.custom;

import java.util.List;

import com.crude.travelcrew.domain.crew.model.dto.CrewMemberRes;

public interface CustomCrewMemberRepository {

	List<CrewMemberRes> findAllCrewMemberByCrewId(Long crewId);
}
