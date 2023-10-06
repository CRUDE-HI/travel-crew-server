package com.crude.travelcrew.domain.administer.dto.getMember;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;

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
public class ADMemberListResponseDto {
	private Long memberId;
	private String email;
	private String nickName;
	private MemberStatus memberStatus;
	private MemberRole role;

	private double heartBeat;
	private int reportCnt;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}