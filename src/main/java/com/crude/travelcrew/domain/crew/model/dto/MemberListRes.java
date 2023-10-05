package com.crude.travelcrew.domain.crew.model.dto;

public class MemberListRes {
	private Long memberId;
	private String nickname;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
