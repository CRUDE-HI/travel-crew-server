package com.crude.travelcrew.domain.administer.service;

import static com.crude.travelcrew.global.error.type.CommonErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.administer.dto.getReport.ADGetReportRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListReq;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListResponseDto;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.report.model.entity.Report;
import com.crude.travelcrew.domain.report.repository.ReportRepository;
import com.crude.travelcrew.global.error.exception.CommonException;

@Service
public class AdminGetReportService {

	@Autowired
	ReportRepository reportRepository;

	@Autowired
	MemberRepository memberRepository;

	public List<ADReportListResponseDto> convertToDto(List<Report> reports) {
		return reports.stream()
			.map(
				report -> {
					Member member = report.getReporter();
					return new ADReportListResponseDto(
						report.getReportId(),
						report.getContent(),
						member.getNickname(),
						report.getCreatedAt(),
						report.getUpdatedAt()
					);
				}
			).collect(Collectors.toList());
	}

	@Transactional
	public ADReportListRes getList(ADReportListReq ADReportListReq) {

		Page<Report> page = reportRepository.findReportsByReportedId(ADReportListReq.getReportedId(),
			ADReportListReq.pageable());

		ADReportListRes ADReportListRes = new ADReportListRes(
			convertToDto(page.getContent()), (int)page.getTotalElements(), ADReportListReq.getPage()
		);

		return ADReportListRes;
	}

	@Transactional
	public ADGetReportRes getReport(Long reportId) {
		Report report = reportRepository.findById(reportId)
			.orElseThrow(() -> new CommonException(REPORT_NOT_FOUND));

		Member reported = report.getReported();
		Member reporter = report.getReporter();

		return new ADGetReportRes(
			report.getReportId(),
			reported.getNickname(),
			report.getContent(),
			reporter.getNickname()
		);
	}
}
