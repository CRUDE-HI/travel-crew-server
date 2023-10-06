package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.crew.model.dto.CrewCommentReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewCommentRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewComment;
import com.crude.travelcrew.domain.crew.repository.CrewCommentRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.CrewErrorCode;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrewService {

	private final CrewRepository crewRepository;
	private final CrewCommentRepository crewCommentRepository;
	private final MemberRepository memberRepository;

	//글 생성
	public CrewRes createCrew(CrewReq requestDto) {
		Crew crew = new Crew(requestDto);
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Member member = memberRepository.findByEmail(email);
		crew.setMember(member);
		crewRepository.save(crew);
		return new CrewRes(crew);
	}

	//수정
	@Transactional
	public Long updateCrew(Long crewId, CrewReq requestDto) {
		Crew crew = crewRepository.findById(crewId).orElseThrow(
			() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다. ")
		);
		crew.update(requestDto);
		return crew.getCrewId();
	}

	//삭제
	public Long deleteCrew(Long crewId) {
		crewRepository.deleteById(crewId);
		return crewId;
	}

	// 전체 조회
	@Transactional
	public List<CrewListRes> getCrewList(String keyword, Pageable pageable) {
		List<Crew> list = crewRepository.findByKeyword(keyword, pageable);
		return list.stream().map(CrewListRes::getEntity).collect(Collectors.toList());
	}

	public long validateToken() {
		// 토큰에 담긴 사용자 정보가 실제로 member 테이블에 존재하는지 여부를 검증
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Member member = memberRepository.findByEmail(email);
		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
		return member.getId();
	}

	// 댓글 조회
	public List<CrewCommentRes> getCommentList(long crewId, Pageable pageable) {
		// 무한 스크롤 추가 예정
		List<CrewComment> list = crewCommentRepository.findByCrewId(crewId, pageable);
		return list.stream().map(CrewCommentRes::fromEntity).collect(Collectors.toList());
	}

	// 댓글 등록
	public void createComment(long crewId, CrewCommentReq commentReq) {
		long memberId = validateToken();
		CrewComment comment = CrewComment.builder()
			.crewId(crewId)
			.memberId(memberId)
			.content(commentReq.getContent())
			.build();
		crewCommentRepository.save(comment);
	}

	// 댓글 수정
	public void modifyComment(long commentId, CrewCommentReq commentReq) {
		long memberId = validateToken();
		CrewComment comment = crewCommentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(CrewErrorCode.COMMENT_NOT_FOUND));
		if (memberId != comment.getMemberId()) {
			throw new CrewException(CrewErrorCode.FAIL_TO_MODIFY_CREW_COMMENT);
		}
		comment.setContent(commentReq.getContent());
		crewCommentRepository.save(comment);
	}

	// 댓글 삭제
	public void deleteComment(long commentId) {
		long memberId = validateToken();
		CrewComment comment = crewCommentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(CrewErrorCode.COMMENT_NOT_FOUND));
		if (memberId != comment.getMemberId()) {
			throw new CrewException(CrewErrorCode.FAIL_TO_DELETE_CREW_COMMENT);
		}
		crewCommentRepository.delete(comment);
	}

}
