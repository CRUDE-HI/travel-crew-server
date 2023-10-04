package com.crude.travelcrew.domain.member.dto;

import lombok.Getter;

@Getter
public class UpdatePWReq {
	private String currentPassword;
	private String newPassword;
	private String validPassword;
}
