package com.crude.travelcrew.domain.member.model.dto;

import java.time.LocalDate;

import com.crude.travelcrew.domain.member.model.constants.Gender;

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
