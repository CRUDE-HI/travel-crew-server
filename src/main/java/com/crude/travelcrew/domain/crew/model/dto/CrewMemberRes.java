package com.crude.travelcrew.domain.crew.model.dto;

import com.crude.travelcrew.domain.crew.model.constants.CrewMemberStatus;
import com.crude.travelcrew.domain.crew.model.entity.CrewMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMemberRes {

	private String profileImgUrl;

	private String nickname;

	private String content;

	private CrewMemberStatus status;

	public static Object fromEntity(CrewMember crewMember) {
		return CrewMemberRes.builder()
			.nickname(crewMember.getMember().getNickname())
			.profileImgUrl(crewMember.getMember().getProfileImgUrl())
			.build();
	}
}
