package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;
import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import com.crude.travelcrew.global.awss3.service.AwsS3Service;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.CrewErrorCode;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrewService {

	private final static String DIR = "crew";

	private final CrewRepository crewRepository;
	private final CrewCommentRepository crewCommentRepository;
	private final MemberRepository memberRepository;
	private final AwsS3Service awsS3Service;

	@Transactional
	public CrewRes createCrew(CrewReq requestDto, MultipartFile image, String email) {

		Member member = memberRepository.findByEmail(email);

		if(Objects.isNull(member)){
			throw new MemberException(MEMBER_NOT_FOUND);
		}

		Crew crew = Crew.builder()
			.title(requestDto.getTitle())
			.crewPlace(requestDto.getCrewPlace())
			.crewStatus(requestDto.getCrewStatus())
			.maxCrew(requestDto.getMaxCrew())
			.travelStart(requestDto.getTravelStart())
			.travelEnd(requestDto.getTravelEnd())
			.latitude(requestDto.getLatitude())
			.longitude(requestDto.getLongitude())
			.crewContent(requestDto.getCrewContent())
			.build();

		crew.setMember(member);
		crewRepository.save(crew);

		if (Objects.isNull(image)) {
			throw new IllegalArgumentException("미리보기 이미지가 없습니다. ");
		}

		String imageUrl = awsS3Service.uploadImageFile(image, DIR);

		crew.setThumbnailImgUrl(imageUrl);
		return CrewRes.fromEntity(crew);
	}

	//수정
	@Transactional
	public CrewRes updateCrew(Long crewId,  CrewReq request, String email){
		// 게시물 없을때
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(()->new CrewException(CREW_NOT_FOUND));

		// 작성자가 아닐떄
		if (!Objects.equals(crew.getMember().getEmail(), email)) {
			throw new CrewException(FAIL_TO_UPDATE_CREW);
		}

		crew.update(request);
		return CrewRes.fromEntity(crew);
	}

	//삭제
	@Transactional
	public Map<String, String> deleteCrew(Long crewId, String email) {

		//해당하는 동행글이 존재하지 않습니다.
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(()->new CrewException(CREW_NOT_FOUND));

		//동행글 삭제는 작성자만 가능합니다.
		if (!Objects.equals(crew.getMember().getEmail(), email)){
			throw new CrewException(FAIL_TO_DELETE_CREW);
		}

		// Thumbnail 삭제
		String crewImageUrl = crew.getThumbnailImgUrl();
		if (crewImageUrl != null) {
			awsS3Service.deleteImageFile(crewImageUrl, DIR);
		}

		// 동행 기록 삭제
		crewRepository.deleteById(crewId);

		// 동행 기록 댓글 변경
		List<CrewComment> crewComments = crewCommentRepository.findAllByCrewId(crewId);

		for (CrewComment comment : crewComments){
			comment.setCrewId(0L);
			crewCommentRepository.save(comment);
		}

		return getMessage("동행 기록이 삭제되었습니다.");

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

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}

}
