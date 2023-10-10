package com.crude.travelcrew.domain.crew.model.dto;

import com.crude.travelcrew.domain.crew.model.constants.ProposalStatus;
import com.crude.travelcrew.domain.crew.model.entity.Proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalRes {

	private String profileImgUrl;

	private String nickname;

	private String content;

	private ProposalStatus status;

	public static ProposalRes fromEntity(Proposal proposal) {
		return ProposalRes.builder()
			.nickname(proposal.getMember().getNickname())
			.profileImgUrl(proposal.getMember().getProfileImgUrl())
			.build();
	}
}
