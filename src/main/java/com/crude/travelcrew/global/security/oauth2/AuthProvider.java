package com.crude.travelcrew.global.security.oauth2;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Getter
public class AuthProvider {

	private String tokenUri;
	private String code;
	private String grantType;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String userInfoUri;

}