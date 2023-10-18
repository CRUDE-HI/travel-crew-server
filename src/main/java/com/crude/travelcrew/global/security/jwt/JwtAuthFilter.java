package com.crude.travelcrew.global.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String[] EXCLUDED_URL
		= {"/api/member/sign-up", "/api/member/login", "/api/member/duplicate/email", "/api/member/duplicate/nickname",
		"/api/member/email/send", "/api/member/email/verify"};

	private static final String REISSUE_TOKEN_URL = "/api/member/token";
	private static final String SSE_CONNECT_URL = "/api/sse/connect";

	private final JwtProvider jwtProvider;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return Arrays.stream(EXCLUDED_URL).anyMatch(request.getRequestURI()::startsWith);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain) throws ServletException, IOException {

		String token;

		if (request.getRequestURI().startsWith(SSE_CONNECT_URL)) { // sse 연결 요청인 경우
			log.info("resolve access token from query string");
			token = request.getQueryString().split("=")[1];
		} else if (request.getRequestURI().startsWith(REISSUE_TOKEN_URL)) { // token 재발급 요청인 경우
			log.info("resolve refresh token from header");
			// header에서 refresh token 추출
			token = request.getHeader("REFRESH-TOKEN");
		} else {
			// header에서 access token 추출
			log.info("resolve access token from header");
			token = jwtProvider.resolveToken(request);
		}

		// token 유효성 검증
		if (!Objects.isNull(token) && jwtProvider.validateToken(token)) {
			log.info("token is valid");

			Authentication authentication = jwtProvider.getAuthentication(token);

			log.info("save authentication object in SecurityContext");
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}
