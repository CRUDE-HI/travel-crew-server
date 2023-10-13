package com.crude.travelcrew.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * cors 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173")
			.allowedHeaders("*")
			.allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
			.maxAge(3600)
			.allowCredentials(true);
	}
}
