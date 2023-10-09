package com.crude.travelcrew.domain.crew.model.dto;

import com.crude.travelcrew.domain.crew.model.constants.CrewMemberStatus;

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
}
