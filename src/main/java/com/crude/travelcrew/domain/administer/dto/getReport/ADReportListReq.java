package com.crude.travelcrew.domain.administer.dto.getReport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
public class ADReportListReq {
	private int page;
	private int size;
	private String search;

	private Long reportedId;

	public Pageable pageable() {
		return PageRequest.of(page, size, Sort.by("createdAt").descending());
	}

	public void getReportedId(Long memberId) {
		this.reportedId = memberId;
	}
}
