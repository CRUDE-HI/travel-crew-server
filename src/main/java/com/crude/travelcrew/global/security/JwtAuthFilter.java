package com.crude.travelcrew.global.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain) throws ServletException, IOException {
		String token = jwtProvider.resolveToken(request);
		if (token != null && jwtProvider.validateToken(token)) {
			// 수정사항 1. Redis에 저장되는 key와 value를 가져오는 key가 다름
			// MemberService line 60 "JWT_ACCESS_TOKEN:" + email 형식에 맞게 아래 코드를 변경
			String key = "JWT_ACCESS_TOKEN:" + jwtProvider.getEmail(token);
			if (redisTemplate.hasKey(key)) {
				String storedToken = redisTemplate.opsForValue().get(key);
				// value in redis: token + expired date
				// token   storedToken
				// null    null        can not be happen because line 30
				// null    notNull     can not be happen because line 30
				// notNull null        result of line 39 => false
				// notNull notNull     compare request token, token in redis in line 39
				Authentication authentication = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}
