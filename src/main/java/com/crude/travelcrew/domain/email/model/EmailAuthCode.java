package com.crude.travelcrew.domain.email.model;

import lombok.Builder;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash("AuthCode")
public class EmailAuthCode {

    @Id
    private String id; // email

    private String code;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;
}
