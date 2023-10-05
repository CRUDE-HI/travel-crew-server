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
public class ReportListRes {
	private List<ReportListResponseDto> reportList;
	private int totalReports;
	private int currentPage;
}
