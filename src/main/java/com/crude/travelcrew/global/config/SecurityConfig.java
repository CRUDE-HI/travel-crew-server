package com.crude.travelcrew.global.config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.global.security.handler.CustomAccessDeniedHandler;
import com.crude.travelcrew.global.security.handler.CustomAuthenticationEntryPoint;
import com.crude.travelcrew.global.security.jwt.JwtAuthFilter;
import com.crude.travelcrew.global.security.oauth2.OAuth2Filter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final OAuth2Filter oAuth2Filter;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
		return http
			.cors()

			.and()
			.httpBasic().disable()
			.csrf().disable()
			.formLogin().disable()
			.headers().frameOptions().sameOrigin()

			.and()
			.authorizeRequests()
			.antMatchers("/api/member/sign-up", "/api/member/login", "/api/member/duplicate/email/**",
				"/api/member/duplicate/nickname/**", "/api/member/email/send",
				"/api/member/email/verify", "/h2-console/**").permitAll()

			// ADMIN, MANAGER 권한이 있는 사람만 관리자 페이지 접근가능
			.antMatchers("/api/admin/**").hasAnyAuthority(MemberRole.ADMIN.getValue(), MemberRole.MANAGER.getValue())
			.anyRequest().authenticated()

			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler)

			.and()
			.addFilterAfter(oAuth2Filter, JwtAuthFilter.class)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClient.newHttpClient();
	}
}