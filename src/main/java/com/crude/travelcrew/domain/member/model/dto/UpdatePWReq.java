package com.crude.travelcrew.domain.member.model.dto;

import lombok.Getter;

@Getter
public class UpdatePWReq {
	private String currentPassword;
	private String newPassword;
	private String validPassword;
}
