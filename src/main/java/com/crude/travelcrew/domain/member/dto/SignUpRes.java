package com.crude.travelcrew.domain.member.dto;

import com.crude.travelcrew.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignUpRes {
	private String email;
	private String nickname;
	private String accesstoken;
	private String refreshtoken;

	public SignUpRes(Member member) {
		this.email = member.getEmail();
		this.nickname = member.getNickname();
	}
}
