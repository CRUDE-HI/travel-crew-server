package com.crude.travelcrew.global.security.oauth2;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

	@Bean
	public HttpClient httpClient() {
		return java.net.http.HttpClient.newHttpClient();
	}

}