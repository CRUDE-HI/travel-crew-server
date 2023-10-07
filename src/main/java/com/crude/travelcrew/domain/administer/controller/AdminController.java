package com.crude.travelcrew.domain.administer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.administer.dto.getMember.ADGetMemberRes;
import com.crude.travelcrew.domain.administer.dto.getMember.ADGetRecordRes;
import com.crude.travelcrew.domain.administer.dto.getMember.ADGetReportRes;
import com.crude.travelcrew.domain.administer.dto.getMember.ADMemberListReq;
import com.crude.travelcrew.domain.administer.dto.getMember.ADMemberListRes;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListReq;
import com.crude.travelcrew.domain.administer.dto.getRecord.ADRecordListRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListReq;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportListRes;
import com.crude.travelcrew.domain.administer.dto.getReport.ADReportedMemberListReq;
import com.crude.travelcrew.domain.administer.dto.adminMember.ADUpdateMemberReq;
import com.crude.travelcrew.domain.administer.service.AdminGetMemberService;
import com.crude.travelcrew.domain.administer.service.AdminGetRecordService;
import com.crude.travelcrew.domain.administer.service.AdminGetReportService;
import com.crude.travelcrew.domain.administer.service.AdminMemberService;
import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.crude.travelcrew.domain.member.model.dto.SignUpRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.model.entity.MemberProfile;

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
	AdminGetRecordService adminGetRecordService;

	@Autowired
	AdminGetReportService adminGetReportService;

	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq) throws Exception {
		try {
			Member memberEntity = new Member(signUpReq);
			MemberProfile memberProfileEntity = new MemberProfile(signUpReq);
			log.info("/api/admin/sign-up request... userInfo : {}", memberEntity);
			Member member = adminMemberService.signUp(memberEntity);

			MemberProfile memberProfile = adminMemberService.setProfile(memberProfileEntity,
				adminMemberService.getByCredential(member.getEmail()));
			return ResponseEntity.ok().body(new SignUpRes(member));
		} catch (Exception e) {
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<SignUpRes> login(@RequestBody SignUpReq signUpReq) throws Exception {
		System.out.println("login: " + signUpReq);
		return ResponseEntity.ok(adminMemberService.login(signUpReq));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() throws Exception {
		System.out.println("logout");
		adminMemberService.logout();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/member")
	public ResponseEntity<ADMemberListRes> memberList(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) throws Exception {

		ADMemberListReq ADMemberListReq = new ADMemberListReq();
		ADMemberListReq.setPage(page);
		ADMemberListReq.setSize(size);
		ADMemberListReq.setSearch(search);

		ADMemberListRes ADMemberListRes = adminGetMemberService.getList(ADMemberListReq);

		return ResponseEntity.ok(ADMemberListRes);
	}

	@GetMapping("/member/{id}")
	public ResponseEntity<ADGetMemberRes> getMember(@PathVariable Long id) {
		ADGetMemberRes memberResponseDto = adminGetMemberService.getMember(id);
		return ResponseEntity.ok(memberResponseDto);
	}

	@PatchMapping("/member/{id}")
	public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody ADUpdateMemberReq updateMemberReq) {
		adminGetMemberService.updateMember(id, updateMemberReq);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/reported-members")
	public ResponseEntity<Page<Member>> getReportedMembers(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) throws Exception {

		ADReportedMemberListReq memberListReq = new ADReportedMemberListReq();
		memberListReq.setPage(page);
		memberListReq.setSize(size);
		memberListReq.setSearch(search);

		Page<Member> reportedMembers = adminGetMemberService.getReportedMembers(memberListReq.pageable());
		return ResponseEntity.ok(reportedMembers);
	}

	@GetMapping("/reported-members/{memberId}")
	public ResponseEntity<ADReportListRes> getReports(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search,
		@PathVariable Long memberId
	) throws Exception {

		ADReportListReq ADReportListReq = new ADReportListReq();
		ADReportListReq.setPage(page);
		ADReportListReq.setSize(size);
		ADReportListReq.setSearch(search);
		ADReportListReq.getReportedId(memberId);

		ADReportListRes ADReportListRes = adminGetReportService.getList(ADReportListReq);

		return ResponseEntity.ok(ADReportListRes);
	}

	@GetMapping("/report/{reportId}")
	public ResponseEntity<ADGetReportRes> getReport(@PathVariable Long reportId) {
		ADGetReportRes reportRes = adminGetReportService.getReport(reportId);
		return ResponseEntity.ok(reportRes);
	}

	@GetMapping("/record")
	public ResponseEntity<ADRecordListRes> recordList(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) throws Exception {

		ADRecordListReq ADRecordListReq = new ADRecordListReq();
		ADRecordListReq.setPage(page);
		ADRecordListReq.setSize(size);
		ADRecordListReq.setSearch(search);

		ADRecordListRes ADRecordListRes = adminGetRecordService.getList(ADRecordListReq);

		return ResponseEntity.ok(ADRecordListRes);
	}

	@GetMapping("/record/{recordId}")
	public ResponseEntity<ADGetRecordRes> getRecord(@PathVariable Long recordId) {
		ADGetRecordRes recordRes = adminGetRecordService.getRecord(recordId);
		return ResponseEntity.ok(recordRes);
	}

	@PatchMapping("/record/{recordId}")
	public ResponseEntity<Void> blockRecord(@PathVariable Long recordId) {
		adminGetRecordService.blockAndDeleteImages(recordId);
		return ResponseEntity.noContent().build();
	}
}
