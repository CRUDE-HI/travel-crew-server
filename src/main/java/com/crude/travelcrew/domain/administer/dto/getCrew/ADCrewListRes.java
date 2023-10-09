package com.crude.travelcrew.domain.administer.dto.getCrew;

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
public class ADCrewListRes {
	private List<ADCrewListResponseDto> crewListResponseDtos;
	private int totalCrews;
	private int currentPage;
}
