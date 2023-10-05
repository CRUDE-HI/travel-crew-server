package com.crude.travelcrew.domain.report.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.report.model.dto.ReportReq;
import com.crude.travelcrew.domain.report.model.dto.ReportRes;
import com.crude.travelcrew.domain.report.model.entity.Report;
import com.crude.travelcrew.domain.report.service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

	@Autowired
	ReportService reportService;

	@PostMapping("/{memberId}")
	public ResponseEntity<ReportRes> memberReport(
			@PathVariable Long memberId,
			Principal principal,
			@RequestBody ReportReq reportReq) {
		Report report = reportService.reportMember(memberId, principal.getName(), reportReq);
		ReportRes response = new ReportRes(report.getReportId(), "신고가 정상적으로 접수되었습니다.");
		return ResponseEntity.ok(response);
	}

}
