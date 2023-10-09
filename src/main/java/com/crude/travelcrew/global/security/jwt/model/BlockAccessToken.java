package com.crude.travelcrew.global.security.jwt.model;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash("BlockAccessToken")
public class BlockAccessToken {

	@Id
	private String id; // access token

	private String email;

	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private Long expiration;
}

