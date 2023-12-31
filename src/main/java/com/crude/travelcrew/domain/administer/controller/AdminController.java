package com.crude.travelcrew.domain.administer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.administer.dto.adminMember.ADUpdateMemberReq;
import com.crude.travelcrew.domain.administer.dto.getCrew.ADCrewListReq;
import com.crude.travelcrew.domain.administer.dto.getCrew.ADCrewListRes;
import com.crude.travelcrew.domain.administer.dto.getCrew.ADGetCrewRes;
import com.crude.travelcrew.domain.administer.dto.getMember.ADGetMemberRes;
import com.crude.travelcrew.domain.administer.dto.getMember.ADMemberListReq;
import com.crude.travelcrew.domain.administer.dto.getMember.ADMemberListRes;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADGetRecordRes;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListReq;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADGetReportRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListReq;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportedMemberListReq;
import com.crude.travelcrew.domain.administer.service.AdminGetCrewService;
import com.crude.travelcrew.domain.administer.service.AdminGetMemberService;
import com.crude.travelcrew.domain.administer.service.AdminGetRecordService;
import com.crude.travelcrew.domain.administer.service.AdminGetReportService;
import com.crude.travelcrew.domain.administer.service.AdminMemberService;
import com.crude.travelcrew.domain.crew.model.dto.CrewCommentRes;
import com.crude.travelcrew.domain.crew.service.CrewService;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.dto.RecordCommentListRes;
import com.crude.travelcrew.domain.record.service.RecordCommentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

	@Autowired
	AdminMemberService adminMemberService;

	@Autowired
	AdminGetMemberService adminGetMemberService;

	@Autowired
	AdminGetCrewService adminGetCrewService;

	@Autowired
	AdminGetRecordService adminGetRecordService;

	@Autowired
	AdminGetReportService adminGetReportService;

	@Autowired
	CrewService crewService;

	@Autowired
	RecordCommentService recordCommentService;

	@PostMapping("/logout")
	public ResponseEntity<String> logout() throws Exception {
		System.out.println("logout");
		adminMemberService.logout();
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/member")
	public ResponseEntity<ADMemberListRes> memberList(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) {

		ADMemberListReq ADMemberListReq = new ADMemberListReq();
		ADMemberListReq.setPage(page);
		ADMemberListReq.setSize(size);
		ADMemberListReq.setSearch(search);

		ADMemberListRes ADMemberListRes = adminGetMemberService.getList(ADMemberListReq);

		return ResponseEntity.ok(ADMemberListRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/member/{id}")
	public ResponseEntity<ADGetMemberRes> getMember(@PathVariable Long id) {
		ADGetMemberRes memberResponseDto = adminGetMemberService.getMember(id);
		return ResponseEntity.ok(memberResponseDto);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PatchMapping("/member/{id}")
	public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody ADUpdateMemberReq updateMemberReq) {
		adminGetMemberService.updateMember(id, updateMemberReq);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/member/report")
	public ResponseEntity<Page<Member>> getReportedMembers(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) {

		ADReportedMemberListReq memberListReq = new ADReportedMemberListReq();
		memberListReq.setPage(page);
		memberListReq.setSize(size);
		memberListReq.setSearch(search);

		Page<Member> reportedMembers = adminGetMemberService.getReportedMembers(memberListReq.pageable());
		return ResponseEntity.ok(reportedMembers);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/reported-members/{memberId}")
	public ResponseEntity<ADReportListRes> getReports(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search,
		@PathVariable Long memberId
	) {

		ADReportListReq ADReportListReq = new ADReportListReq();
		ADReportListReq.setPage(page);
		ADReportListReq.setSize(size);
		ADReportListReq.setSearch(search);
		ADReportListReq.getReportedId(memberId);

		ADReportListRes ADReportListRes = adminGetReportService.getList(ADReportListReq);

		return ResponseEntity.ok(ADReportListRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/reported-members/{memberId}/{reportId}")
	public ResponseEntity<ADGetReportRes> getReport(@PathVariable Long reportId) {
		ADGetReportRes reportRes = adminGetReportService.getReport(reportId);
		return ResponseEntity.ok(reportRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/crew")
	public ResponseEntity<ADCrewListRes> getCrew(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) {

		ADCrewListReq ADCrewListReq = new ADCrewListReq();
		ADCrewListReq.setPage(page);
		ADCrewListReq.setSize(size);
		ADCrewListReq.setSearch(search);

		ADCrewListRes ADCrewListRes = adminGetCrewService.getList(ADCrewListReq);

		return ResponseEntity.ok(ADCrewListRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/crew/{crewId}")
	public ResponseEntity<ADGetCrewRes> getCrew(@PathVariable Long crewId) {
		ADGetCrewRes crewRes = adminGetCrewService.getCrew(crewId);
		return ResponseEntity.ok(crewRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@PatchMapping("/crew/{crewId}")
	public ResponseEntity<Void> blockCrew(@PathVariable Long crewId) {
		adminGetCrewService.blockAndDeleteImages(crewId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/crew/{crewId}/comment")
	public ResponseEntity<List<CrewCommentRes>> getCrewCommentList(@PathVariable long crewId, Pageable pageable) {
		return ResponseEntity.ok(crewService.getCommentList(crewId, pageable));
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@PatchMapping("/crew/{crewId}/comment/{commentId}")
	public ResponseEntity<Object> blockCrewComment(@PathVariable long commentId) {
		adminGetCrewService.blockComment(commentId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/record")
	public ResponseEntity<ADRecordListRes> recordList(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) {

		ADRecordListReq ADRecordListReq = new ADRecordListReq();
		ADRecordListReq.setPage(page);
		ADRecordListReq.setSize(size);
		ADRecordListReq.setSearch(search);

		ADRecordListRes ADRecordListRes = adminGetRecordService.getList(ADRecordListReq);

		return ResponseEntity.ok(ADRecordListRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/record/{recordId}")
	public ResponseEntity<ADGetRecordRes> getRecord(@PathVariable Long recordId) {
		ADGetRecordRes recordRes = adminGetRecordService.getRecord(recordId);
		return ResponseEntity.ok(recordRes);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@PatchMapping("/record/{recordId}")
	public ResponseEntity<Void> blockRecord(@PathVariable Long recordId) {
		adminGetRecordService.blockAndDeleteImages(recordId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@GetMapping("/record/{recordId}/comment")
	public ResponseEntity<RecordCommentListRes> recordCommentList(@PathVariable Long recordId, Pageable pageable) {
		return ResponseEntity.ok(recordCommentService.getCommentList(recordId, pageable));
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	@PatchMapping("/record/{recordId}/comment/{commentId}")
	public ResponseEntity<Object> blockRecordComment(@PathVariable Long recordId, @PathVariable Long commentId) {
		adminGetRecordService.blockComment(recordId, commentId);
		return ResponseEntity.noContent().build();
	}
}
