package com.crude.travelcrew.domain.member.controller;

import java.security.Principal;
import java.util.List;

import com.crude.travelcrew.domain.record.model.dto.RecordHeartRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.member.model.dto.MemberRes;
import com.crude.travelcrew.domain.member.model.dto.UpdateNickReq;
import com.crude.travelcrew.domain.member.model.dto.UpdatePWReq;
import com.crude.travelcrew.domain.member.model.dto.WithDrawPW;
import com.crude.travelcrew.domain.member.service.MyPageService;
import com.crude.travelcrew.domain.record.model.dto.MyRecordRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping
	public ResponseEntity<MemberRes> myInfo(Principal principal) {
		return ResponseEntity.ok(myPageService.myInfo(principal.getName()));
	}

	// 닉네임 변경
	@PatchMapping("/nickname")
	public ResponseEntity<Void> updateNick(@RequestBody UpdateNickReq updateNickReq, Principal principal) {
		myPageService.updateNick(updateNickReq, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 비밀번호 변경
	@PatchMapping("/password")
	public ResponseEntity<Void> updatePW(@RequestBody UpdatePWReq updatePWReq, Principal principal) {
		myPageService.updatePW(updatePWReq, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 프로필이미지 업로드
	@PatchMapping("/profile")
	public ResponseEntity<Void> updateProfile(@RequestBody MultipartFile image, Principal principal) {
		myPageService.updateImg(image, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 프로필이미지 삭제
	@DeleteMapping("/profile")
	public ResponseEntity<Void> deleteProfile(Principal principal) {
		myPageService.deleteImg(principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 내가 쓴 동행 글 조회
	@GetMapping("/crew")
	public ResponseEntity<List<CrewRes>> getMyCrewList(Principal principal) {
		return ResponseEntity.ok(myPageService.getMyCrewList(principal.getName()));
	}

	// 내가 쓴 여행기록 글 조회
	@GetMapping("/record")
	public ResponseEntity<List<MyRecordRes>> getMyRecordList(Principal principal) {
		return ResponseEntity.ok(myPageService.getMyRecordList(principal.getName()));
	}

	// 내가 스크랩한 글 조회
	@GetMapping("/scrap")
	public ResponseEntity<List<CrewRes>> prtcpCrew(Principal principal) {
		List<CrewRes> postsList = myPageService.prtcpCrew(principal.getName());
		return ResponseEntity.ok(postsList);
	}

	// 회원 비활성화
	@PutMapping
	public ResponseEntity<Void> withDraw(@RequestBody WithDrawPW withDrawPW, Principal principal) {
		myPageService.withDraw(withDrawPW, principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 내가 신청한 동행글 List 조회
	@GetMapping("/participateList")
	public ResponseEntity<List<CrewRes>> participateCrewList(Principal principal) {
		List<CrewRes> commentCrewList = myPageService.getMyProposalCrewList(principal.getName());
		return ResponseEntity.ok(commentCrewList);
	}

	// 내가 좋아요 한 여행 기록 조회
	@GetMapping("/heart")
	public ResponseEntity<List<RecordHeartRes>> getMyHeartRecordList(Principal principal) {
		List<RecordHeartRes> myHeartRecordList = myPageService.getMyHeartRecordList(principal.getName());
		return ResponseEntity.ok(myHeartRecordList);
	}
}
