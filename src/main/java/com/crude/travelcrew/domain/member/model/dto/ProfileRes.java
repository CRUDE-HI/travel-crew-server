package com.crude.travelcrew.domain.member.model.dto;

import com.crude.travelcrew.domain.member.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRes {

	private String profileImgUrl;

	private String nickname;

	public static ProfileRes fromEntity(Member member) {
		return ProfileRes.builder()
			.profileImgUrl(member.getProfileImgUrl())
			.nickname(member.getNickname())
			.build();
	}
}
