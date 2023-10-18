package com.crude.travelcrew.domain.crew.service;

import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;
import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
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
import com.crude.travelcrew.domain.crew.model.dto.ProposalRes;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewComment;
import com.crude.travelcrew.domain.crew.model.entity.CrewMember;
import com.crude.travelcrew.domain.crew.model.entity.CrewMemberId;
import com.crude.travelcrew.domain.crew.model.entity.CrewMessage;
import com.crude.travelcrew.domain.crew.model.entity.QCrewMessage;
import com.crude.travelcrew.domain.crew.repository.CrewCommentRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.crew.repository.ProposalRepository;
import com.crude.travelcrew.domain.crew.repository.CrewMemberRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.awss3.service.AwsS3Service;
import com.crude.travelcrew.global.error.exception.CommonException;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.CommonErrorCode;
import com.crude.travelcrew.global.error.type.CrewErrorCode;
import com.crude.travelcrew.global.security.jwt.JwtProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrewServiceImpl implements CrewService {

	private final static String DIR = "crew";

	private final static String CHAT_EXCHANGE_NAME = "crew.exchange";

	private final CrewRepository crewRepository;
	private final CrewCommentRepository crewCommentRepository;
	private final MemberRepository memberRepository;
	private final ProposalRepository proposalRepository;
	private final AwsS3Service awsS3Service;
	private final CrewMemberRepository crewMemberRepository;
	private final JwtProvider jwtProvider;
	private final JPAQueryFactory queryFactory;

	@Override
	@Transactional
	public CrewRes createCrew(CrewReq requestDto, MultipartFile image, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

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

		// 작성자가 크루 멤버에 Owner로 저장
		CrewMemberId crewMemberId = new CrewMemberId(member.getId(), crew.getCrewId());

		CrewMember crewMember = CrewMember.builder()
			.id(crewMemberId)
			.isOwner(true)
			.build();

		crewMemberRepository.save(crewMember);

		// 썸네일 이미지 저장
		if (Objects.isNull(image)) {
			throw new IllegalArgumentException("미리보기 이미지가 없습니다. ");
		}

		String imageUrl = awsS3Service.uploadImageFile(image, DIR);

		crew.setThumbnailImgUrl(imageUrl);

		return CrewRes.fromEntity(crew);
	}

	//수정
	@Override
	@Transactional
	public CrewRes updateCrew(Long crewId, CrewReq request, String email) {
		// 게시물 없을때
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		// 작성자가 아닐떄
		if (!Objects.equals(crew.getMember().getEmail(), email)) {
			throw new CrewException(FAIL_TO_UPDATE_CREW);
		}

		crew.update(request);
		return CrewRes.fromEntity(crew);
	}

	//삭제
	@Override
	@Transactional
	public Map<String, String> deleteCrew(Long crewId, String email) {

		//해당하는 동행글이 존재하지 않습니다.
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		//동행글 삭제는 작성자만 가능합니다.
		if (!Objects.equals(crew.getMember().getEmail(), email)) {
			throw new CrewException(FAIL_TO_DELETE_CREW);
		}

		// Thumbnail 삭제
		String crewImageUrl = crew.getThumbnailImgUrl();
		if (crewImageUrl != null) {
			awsS3Service.deleteImageFile(crewImageUrl, DIR);
		}

		// 동행 신청 멤버 삭제
		proposalRepository.deleteAllByProposalId(crewId);

		// 게시물 댓글 삭제
		crewCommentRepository.deleteAllByCrewId(crewId);

		// 동행 기록 삭제
		crewRepository.deleteById(crewId);

		return getMessage("동행 기록이 삭제되었습니다.");

	}

	// 동행 게시글 상세조회
	@Override
	@Transactional
	public CrewRes crewView(Long id) {
		Crew crew = crewRepository.findById(id)
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));
		List<ProposalRes> proposalList = proposalRepository.findAllByCrewId(crew.getCrewId());
		CrewRes crewDTO = crew.toCrewDTO();
		crewDTO.setProposalList(proposalList);
		crewDTO.setHeartBeat(crew.getMember().getMemberProfile().getHeartBeat());
		crewDTO.setUpdateAt(LocalDateTime.now());
		return crewDTO;
	}

	// 전체 조회
	@Override
	@Transactional
	public Map<String, Object> getCrewList(String keyword, Pageable pageable) {
		Page<Crew> page = crewRepository.findByKeyword(keyword, pageable);
		List<CrewListRes> content = page.getContent().stream().map(CrewListRes::getEntity).collect(Collectors.toList());
		Map<String, Object> result = new HashMap<>();
		result.put("content", content);
		result.put("totalPages", page.getTotalPages());
		result.put("totalElements", page.getTotalElements());
		return result;
	}

	public long validateToken() {
		// 토큰에 담긴 사용자 정보가 실제로 member 테이블에 존재하는지 여부를 검증
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		if (Objects.isNull(member)) {
			throw new MemberException(MEMBER_NOT_FOUND);
		}
		return member.getId();
	}

	// 댓글 조회
	@Override
	@Transactional
	public List<CrewCommentRes> getCommentList(long crewId, Pageable pageable) {
		List<CrewComment> list = crewCommentRepository.findByCrewId(crewId, pageable);
		return list.stream().map(CrewCommentRes::fromEntity).collect(Collectors.collectingAndThen(
			Collectors.toList(),
			reversedList -> {
				Collections.reverse(reversedList);
				return reversedList;
			}
		));
	}

	// 댓글 등록
	@Override
	@Transactional
	public void createComment(long crewId, String email, CrewCommentReq commentReq) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		CrewComment comment = CrewComment.builder()
			.crewId(crewId)
			.member(member)
			.content(commentReq.getContent())
			.build();
		crewCommentRepository.save(comment);
	}

	// 댓글 수정
	@Override
	@Transactional
	public void modifyComment(long commentId, String email, CrewCommentReq commentReq) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		CrewComment comment = crewCommentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(CrewErrorCode.COMMENT_NOT_FOUND));
		if (!Objects.equals(member.getId(), comment.getMember().getId())) {
			throw new CrewException(CrewErrorCode.FAIL_TO_MODIFY_CREW_COMMENT);
		}
		comment.setContent(commentReq.getContent());
		crewCommentRepository.save(comment);
	}

	// 댓글 삭제
	@Override
	@Transactional
	public void deleteComment(long commentId, String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		CrewComment comment = crewCommentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(CrewErrorCode.COMMENT_NOT_FOUND));
		if (!Objects.equals(member.getId(), comment.getMember().getId())) {
			throw new CrewException(CrewErrorCode.FAIL_TO_DELETE_CREW_COMMENT);
		}
		crewCommentRepository.delete(comment);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}


	// 채팅방 입장
	@Override
	@Transactional
	public void enterCrewChat(Long crewId, String token) {
		// 1. Token에서 사용자 인증 정보 가져오기
		if (!jwtProvider.validateToken(token)) {
			throw new CommonException(CommonErrorCode.FAIL_TO_AUTHENTICATION);
		}
		String userEmail = jwtProvider.getEmail(token);
		Member member = memberRepository.findByEmail(userEmail)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		// 2. 사용자의 채팅방 접근 권한 확인
		CrewMemberId crewMemberId = new CrewMemberId(member.getId(), crewId);
		boolean isCrewMember = crewMemberRepository.existsById(crewMemberId);
		if (!isCrewMember) {
			throw new CrewException(FAIL_ENTER);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<CrewMessage> getChatHistory(Long crewId, int page, int size) {
		QCrewMessage qCrewMessage = QCrewMessage.crewMessage;

		return queryFactory
			.selectFrom(qCrewMessage)
			.where(qCrewMessage.crewMember.id.crewId.eq(crewId))
			.orderBy(qCrewMessage.createdAt.desc())
			.offset(page * size)
			.limit(size)
			.fetch();
	}

}
