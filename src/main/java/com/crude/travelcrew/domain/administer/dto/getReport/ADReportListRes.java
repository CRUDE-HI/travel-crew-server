package com.crude.travelcrew.domain.administer.dto.getReport;

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
public class ADReportListRes {
	private List<ADReportListResponseDto> reportList;
	private int totalReports;
	private int currentPage;
}
