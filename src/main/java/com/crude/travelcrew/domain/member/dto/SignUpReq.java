package com.crude.travelcrew.domain.member.dto;

import java.time.LocalDate;

import com.crude.travelcrew.domain.member.constants.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignUpReq {
	private String email;
	private String password;
	private String nickname;

	private String name;
	private LocalDate birth;
	private Gender gender;
}
