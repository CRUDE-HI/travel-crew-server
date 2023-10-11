package com.crude.travelcrew.domain.crew.repository.custom;

import java.util.List;
import java.util.Optional;

import com.crude.travelcrew.domain.crew.model.constants.ProposalStatus;
import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;
import com.crude.travelcrew.domain.crew.model.entity.Proposal;

public interface CustomProposalRepository {

	List<ProposalRes> findAllByCrewId(Long crewId);

	Long deleteAllByProposalId(Long proposalId);

	Optional<Proposal> findByCrewIdAndNicknameAndProposalStatus(Long crewId, String nickname, ProposalStatus status);
}
