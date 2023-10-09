package com.crude.travelcrew.domain.member.model.dto;

import java.time.LocalDate;

import com.crude.travelcrew.domain.member.model.constants.Gender;
import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;
import com.crude.travelcrew.domain.member.model.constants.ProviderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberRes {
	private Long memberId;

	private String email;

	private String nickname;

	private String profileImgUrl;

	private MemberStatus memberStatus;

	private ProviderType providerType;

	private LocalDate birth;

	private String name;

	private Gender gender;

	private Double heartBeat;

	private int reportCnt;

}
