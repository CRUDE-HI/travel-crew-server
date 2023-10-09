package com.crude.travelcrew.global.security.jwt.model;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash("RefreshToken")
public class RefreshToken {

	@Id
	private String id; // email

	private String token;

	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private Long expiration;
}
