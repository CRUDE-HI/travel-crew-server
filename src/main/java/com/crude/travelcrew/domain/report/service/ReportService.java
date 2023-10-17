package com.crude.travelcrew.domain.report.service;

import static com.crude.travelcrew.global.error.type.CommonErrorCode.*;
import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.report.model.dto.ReportReq;
import com.crude.travelcrew.domain.report.model.entity.Report;
import com.crude.travelcrew.domain.report.repository.ReportRepository;
import com.crude.travelcrew.global.error.exception.CommonException;
import com.crude.travelcrew.global.error.exception.MemberException;

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

		Member reporter = memberRepository.findByEmail(principalName)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		if (reportedMember.getId().equals(reporter.getId())){
			throw new CommonException(FAIL_TO_REPORT);
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
