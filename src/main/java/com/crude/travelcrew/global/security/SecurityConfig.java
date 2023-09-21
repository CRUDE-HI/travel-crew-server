package com.crude.travelcrew.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
	SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
		http
.authorizeHttpRequests(
			(authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
				// .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
				// .anyRequest().authenticated()
		)
			.csrf(
				(csrf) -> csrf
					.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			)
			.headers(
				(headers)-> headers
					.addHeaderWriter(new XFrameOptionsHeaderWriter(
						XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
			)
			.formLogin(
				(formLogin) -> formLogin
					.loginPage("/")
					.usernameParameter("")
					.defaultSuccessUrl("/")
			)
			.logout(
				(logout) -> logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
			)
		;
		return http.build();
	}

}
