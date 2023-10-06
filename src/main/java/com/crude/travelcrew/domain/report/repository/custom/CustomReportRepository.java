package com.crude.travelcrew.domain.report.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crude.travelcrew.domain.report.model.entity.Report;

public interface CustomReportRepository {
	Page<Report> findReportsByReportedId(Long reportedId, Pageable pageable);
}
