package com.crude.travelcrew.domain.crew.repository.custom;

import static com.crude.travelcrew.domain.crew.model.entity.QProposal.*;
import static com.crude.travelcrew.domain.member.model.entity.QMember.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomProposalRepositoryImpl implements CustomProposalRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProposalRes> findAllByCrewId(Long crewId) {
		return queryFactory.select(fields(ProposalRes.class,
				member.profileImgUrl.as("profileImgUrl"),
				member.nickname.as("nickname"),
				proposal.content.as("content"),
				proposal.status.as("status")
			))
			.from(proposal)
			.leftJoin(proposal.member, member)
			.fetch();
	}

	@Override
	public Long deleteAllByProposalId(Long proposalId){
		return queryFactory.delete(proposal)
			.where(proposal.id.eq(proposalId))
			.execute();
	}
}
