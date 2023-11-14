package com.crude.travelcrew.global.security.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class AuthProvider {

	@Value("${spring.security.oauth2.client.provider.naver.token-uri}")
	private String tokenUri;

	@Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
	private String grantType;

	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
	private String userInfoUri;

	private String code;

	public void setCodeFromRequest(HttpServletRequest request) {
		this.code = request.getParameter("code");
	}

}