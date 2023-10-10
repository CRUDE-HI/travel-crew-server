package com.crude.travelcrew.domain.crew.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.Proposal;
import com.crude.travelcrew.domain.crew.repository.custom.CustomProposalRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface ProposalRepository extends JpaRepository<Proposal, Long>, CustomProposalRepository {

	boolean existsByCrewAndMember(Crew crew, Member member);

	Optional<Proposal> findByCrewAndMember(Crew crew, Member member);

	List<Proposal> findAllByMember(Member member);

	// List<Proposal> findAllByCrewAndMemberNotAndStatus(Crew crew, Member member, ProposalStatus Status);
}
