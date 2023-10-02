package com.crude.travelcrew.domain.member.dto;

import com.crude.travelcrew.global.entity.Authority;
import lombok.Data;

@Data
public class MemberModel {

    private String email;
    private String password;
    private Authority authority;
}
