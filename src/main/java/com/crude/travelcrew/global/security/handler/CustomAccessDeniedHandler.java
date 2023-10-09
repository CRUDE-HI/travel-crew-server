package com.crude.travelcrew.global.security.handler;

import static com.crude.travelcrew.global.error.type.CommonErrorCode.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.crude.travelcrew.global.error.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AccessDeniedException 처리
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setStatus(403);

		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(FAIL_TO_AUTHORIZATION.getStatus().value())
			.code(FAIL_TO_AUTHORIZATION.getCode())
			.message(FAIL_TO_AUTHORIZATION.getMessage())
			.build();

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
