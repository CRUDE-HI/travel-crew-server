package com.crude.travelcrew.domain.member.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
	ADMIN, MANAGER, USER, DROP
    
	ADMIN("ROLE_ADMIN"),
	MANAGER("ROLE_MANAGER"),
	USER("ROLE_USER");

	private final String value;
}
