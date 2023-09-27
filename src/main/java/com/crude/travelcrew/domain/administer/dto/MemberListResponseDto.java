package com.crude.travelcrew.domain.administer.dto;

import java.time.LocalDate;

import com.crude.travelcrew.domain.member.constants.MemberRole;
import com.crude.travelcrew.domain.member.constants.MemberStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberListResponseDto {
	private Long memberId;
	private String email;
	private String nickName;
	private MemberStatus memberStatus;
	private MemberRole role;

	private double heartBeat;
	private int reportCnt;

	private LocalDate createdAt;
	private LocalDate updatedAt;

}