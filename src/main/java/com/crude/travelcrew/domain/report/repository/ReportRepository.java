package com.crude.travelcrew.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crude.travelcrew.domain.report.model.entity.Report;
import com.crude.travelcrew.domain.report.repository.custom.CustomReportRepository;
import com.crude.travelcrew.domain.report.repository.custom.CustomReportRepositoryImpl;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, CustomReportRepository {
}
