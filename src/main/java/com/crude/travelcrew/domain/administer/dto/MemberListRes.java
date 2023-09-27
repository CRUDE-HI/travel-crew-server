package com.crude.travelcrew.domain.administer.dto;

import java.util.List;

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
public class MemberListRes {
	private List<MemberListResponseDto> memberList;
	private int totalMembers;
	private int currentPage;
}
