package com.crude.travelcrew.domain.administer.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.administer.dto.GetReportRes;
import com.crude.travelcrew.domain.administer.dto.ReportListReq;
import com.crude.travelcrew.domain.administer.dto.ReportListRes;
import com.crude.travelcrew.domain.administer.dto.ReportListResponseDto;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.report.model.entity.Report;
import com.crude.travelcrew.domain.report.repository.ReportRepository;

@Service
public class AdminGetReportService {

	@Autowired
	ReportRepository reportRepository;

	@Autowired
	MemberRepository memberRepository;

	public List<ReportListResponseDto> convertToDto(List<Report> reports) {
		return reports.stream()
			.map(
				report -> {
					Member member = report.getReporter();
					return new ReportListResponseDto(
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
	public ReportListRes getList(ReportListReq reportListReq) {

		Page<Report> page = reportRepository.findReportsByReportedId(reportListReq.getReportedId(),
			reportListReq.pageable());

		ReportListRes reportListRes = new ReportListRes(
			convertToDto(page.getContent()), (int)page.getTotalElements(), reportListReq.getPage()
		);

		return reportListRes;
	}

	@Transactional
	public GetReportRes getReport(Long reportId) {
		Report report = reportRepository.findById(reportId)
			.orElseThrow(() -> new IllegalArgumentException("해당 신고를 찾을 수 없습니다."));

		Member reported = report.getReported();
		Member reporter = report.getReporter();

		return new GetReportRes(
			report.getReportId(),
			reported.getNickname(),
			report.getContent(),
			reporter.getNickname()
		);
	}
}
