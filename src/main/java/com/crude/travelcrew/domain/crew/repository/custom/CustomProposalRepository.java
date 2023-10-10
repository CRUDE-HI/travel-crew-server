package com.crude.travelcrew.domain.crew.repository.custom;

import java.util.List;

import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;

public interface CustomProposalRepository {

	List<ProposalRes> findAllByCrewId(Long crewId);

	Long deleteAllByProposalId(Long proposalId);
}
