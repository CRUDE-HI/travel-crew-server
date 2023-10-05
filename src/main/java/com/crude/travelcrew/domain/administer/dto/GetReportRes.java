package com.crude.travelcrew.domain.administer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetReportRes {
	private Long ReportId;
	private String Reported;
	private String content;
	private String Reporter;
}
