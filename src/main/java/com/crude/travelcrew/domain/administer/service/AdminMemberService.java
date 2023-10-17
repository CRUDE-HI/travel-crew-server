package com.crude.travelcrew.domain.administer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.repository.MemberProfileRepository;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.security.jwt.JwtProvider;

@Service
public class AdminMemberService {

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	public void logout() throws Exception {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if (redisTemplate.opsForValue().get("JWT_ACCESS_TOKEN:" + email) != null) {
			redisTemplate.delete("JWT_ACCESS_TOKEN:" + email); //Token 삭제
		}
	}
}
