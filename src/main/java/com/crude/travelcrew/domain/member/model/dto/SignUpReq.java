package com.crude.travelcrew.domain.member.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@NotBlank(message = "닉네임을 입력해주세요.")
	private String nickname;

	@NotBlank(message = "이름을 입력해주세요.")
	private String name;
	private LocalDate birth;
	private Gender gender;
}
