package com.crude.travelcrew.global.security.handler;

import static com.crude.travelcrew.global.error.type.CommonErrorCode.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.crude.travelcrew.global.error.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthenticationException 처리
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setStatus(401);

		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(FAIL_TO_AUTHENTICATION.getStatus().value())
			.code(FAIL_TO_AUTHENTICATION.getCode())
			.message(FAIL_TO_AUTHENTICATION.getMessage())
			.build();

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
