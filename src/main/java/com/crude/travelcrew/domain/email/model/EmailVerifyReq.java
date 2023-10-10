package com.crude.travelcrew.domain.email.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EmailVerifyReq {
    private String email;
    private String authCode;
}
