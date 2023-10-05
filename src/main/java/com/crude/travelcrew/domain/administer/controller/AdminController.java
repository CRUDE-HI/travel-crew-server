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

import com.crude.travelcrew.domain.administer.dto.GetMemberRes;
import com.crude.travelcrew.domain.administer.dto.GetRecordRes;
import com.crude.travelcrew.domain.administer.dto.MemberListReq;
import com.crude.travelcrew.domain.administer.dto.MemberListRes;
import com.crude.travelcrew.domain.administer.dto.RecordListReq;
import com.crude.travelcrew.domain.administer.dto.RecordListRes;
import com.crude.travelcrew.domain.administer.dto.ReportListReq;
import com.crude.travelcrew.domain.administer.dto.ReportListRes;
import com.crude.travelcrew.domain.administer.dto.ReportedMemberListReq;
import com.crude.travelcrew.domain.administer.dto.UpdateMemberReq;
import com.crude.travelcrew.domain.administer.service.AdminGetMemberService;
import com.crude.travelcrew.domain.administer.service.AdminGetRecordService;
import com.crude.travelcrew.domain.administer.service.AdminGetReportService;
import com.crude.travelcrew.domain.administer.service.AdminMemberService;
import com.crude.travelcrew.domain.member.dto.SignUpReq;
import com.crude.travelcrew.domain.member.dto.SignUpRes;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.entity.MemberProfile;

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
	public ResponseEntity<MemberListRes> memberList(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) throws Exception {

		MemberListReq memberListReq = new MemberListReq();
		memberListReq.setPage(page);
		memberListReq.setSize(size);
		memberListReq.setSearch(search);

		MemberListRes memberListRes = adminGetMemberService.getList(memberListReq);

		return ResponseEntity.ok(memberListRes);
	}

	@GetMapping("/member/{id}")
	public ResponseEntity<GetMemberRes> getMember(@PathVariable Long id) {
		GetMemberRes memberResponseDto = adminGetMemberService.getMember(id);
		return ResponseEntity.ok(memberResponseDto);
	}

	@PatchMapping("/member/{id}")
	public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody UpdateMemberReq updateMemberReq) {
		adminGetMemberService.updateMember(id, updateMemberReq);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/reported-members")
	public ResponseEntity<Page<Member>> getReportedMembers(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) throws Exception {

		ReportedMemberListReq memberListReq = new ReportedMemberListReq();
		memberListReq.setPage(page);
		memberListReq.setSize(size);
		memberListReq.setSearch(search);

		Page<Member> reportedMembers = adminGetMemberService.getReportedMembers(memberListReq.pageable());
		return ResponseEntity.ok(reportedMembers);
	}

	@GetMapping("/reported-members/{memberId}")
	public ResponseEntity<ReportListRes> getReports(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search,
		@PathVariable Long memberId
	) throws Exception {

		ReportListReq reportListReq = new ReportListReq();
		reportListReq.setPage(page);
		reportListReq.setSize(size);
		reportListReq.setSearch(search);
		reportListReq.getReportedId(memberId);

		ReportListRes reportListRes = adminGetReportService.getList(reportListReq);

		return ResponseEntity.ok(reportListRes);
	}

	@GetMapping("/record")
	public ResponseEntity<RecordListRes> recordList(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "search", required = false) String search
	) throws Exception {

		RecordListReq recordListReq = new RecordListReq();
		recordListReq.setPage(page);
		recordListReq.setSize(size);
		recordListReq.setSearch(search);

		RecordListRes recordListRes = adminGetRecordService.getList(recordListReq);

		return ResponseEntity.ok(recordListRes);
	}

	@GetMapping("/record/{recordId}")
	public ResponseEntity<GetRecordRes> getRecord(@PathVariable Long recordId) {
		GetRecordRes recordRes = adminGetRecordService.getRecord(recordId);
		return ResponseEntity.ok(recordRes);
	}

	@PatchMapping("/record/{recordId}")
	public ResponseEntity<Void> blockRecord(@PathVariable Long recordId) {
		adminGetRecordService.blockAndDeleteImages(recordId);
		return ResponseEntity.noContent().build();
	}
}
