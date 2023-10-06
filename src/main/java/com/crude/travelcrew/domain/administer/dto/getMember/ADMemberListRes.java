package com.crude.travelcrew.domain.administer.dto.getMember;

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
public class ADMemberListRes {
	private List<ADMemberListResponseDto> memberList;
	private int totalMembers;
	private int currentPage;
}
