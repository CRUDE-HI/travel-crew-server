package com.crude.travelcrew.domain.report.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.report.model.dto.ReportReq;
import com.crude.travelcrew.domain.report.model.entity.Report;
import com.crude.travelcrew.domain.report.repository.ReportRepository;

@Service
public class ReportService {

	@Autowired
	ReportRepository reportRepository;

	@Autowired
	MemberRepository memberRepository;

	@Transactional
	public Report reportMember (Long reported, String principalName, ReportReq reportReq) {
		Member reportedMember = memberRepository.findById(reported)
			.orElseThrow(() -> new EntityNotFoundException("신고당한 멤버의 아이디를 찾을 수 없습니다."));

		Member reporter = memberRepository.findByEmail(principalName);
		if (reporter == null) {
			throw new EntityNotFoundException("신고한 멤버의 아이디를 찾을 수 없습니다.");
		}

		if (reportedMember.getId().equals(reporter.getId())){
			throw new IllegalArgumentException("자신을 신고할 수 없습니다.");
		}

		Report report = Report.builder()
			.reported(reportedMember)
			.reporter(reporter)
			.content(reportReq.getContent())
			.build();

		reportedMember.getMemberProfile().increaseReportCnt();
		return reportRepository.save(report);
	}
}
