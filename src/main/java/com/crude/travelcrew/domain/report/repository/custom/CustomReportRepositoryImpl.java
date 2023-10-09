package com.crude.travelcrew.domain.report.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.crude.travelcrew.domain.report.model.entity.QReport;
import com.crude.travelcrew.domain.report.model.entity.Report;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomReportRepositoryImpl implements CustomReportRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Report> findReportsByReportedId(Long reportedId, Pageable pageable) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QReport report = QReport.report;

		List<Report> reports = queryFactory.selectFrom(report)
			.where(report.reported.id.eq(reportedId))
			.orderBy(report.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory.selectFrom(report)
			.where(report.ReportId.eq(reportedId))
			.fetchCount();

		return new PageImpl<>(reports, pageable, total);
	}
}
