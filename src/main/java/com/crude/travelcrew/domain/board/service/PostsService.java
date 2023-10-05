package com.crude.travelcrew.domain.board.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.board.dto.CommentReq;
import com.crude.travelcrew.domain.board.dto.CommentRes;
import com.crude.travelcrew.domain.board.dto.PostListRes;
import com.crude.travelcrew.domain.board.dto.PostsReq;
import com.crude.travelcrew.domain.board.dto.PostsRes;
import com.crude.travelcrew.domain.board.entity.Comment;
import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.board.repository.CommentRepository;
import com.crude.travelcrew.domain.board.repository.PostsRepository;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.global.error.exception.CrewException;
import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.CrewErrorCode;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostsService {

	private final PostsRepository postsRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;

	//글 생성
	public PostsRes createCrew(PostsReq requestDto) {
		Posts posts = new Posts(requestDto);
		postsRepository.save(posts);
		return new PostsRes(posts);
	}

	//수정
	@Transactional
	public Long updateCrew(Long crewId, PostsReq requestDto) {
		Posts posts = postsRepository.findById(crewId).orElseThrow(
			() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다. ")
		);
		posts.update(requestDto);
		return posts.getCrewId();
	}

	//삭제
	public Long deleteCrew(Long crewId) {
		postsRepository.deleteById(crewId);
		return crewId;
	}

	// 전체 조회
	public List<PostListRes> listPosts(String keyword, Pageable pageable) {
		List<Posts> list = postsRepository.findByKeyword(keyword, pageable);
		return list.stream().map(PostListRes::getEntity).collect(Collectors.toList());
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
	public List<CommentRes> listComment(long crewId, Pageable pageable) {
		// 무한 스크롤 추가 예정
		List<Comment> list = commentRepository.findByCrewId(crewId, pageable);
		return list.stream().map(CommentRes::fromEntity).collect(Collectors.toList());
	}

	// 댓글 등록
	public void createComment(long crewId, CommentReq commentReq) {
		long memberId = validateToken();
		Comment comment = Comment.builder()
			.crewId(crewId)
			.memberId(memberId)
			.content(commentReq.getContent())
			.build();
		commentRepository.save(comment);
	}

	// 댓글 수정
	public void modifyComment(long commentId, CommentReq commentReq) {
		long memberId = validateToken();
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(CrewErrorCode.COMMENT_NOT_FOUND));
		if (memberId != comment.getMemberId()) {
			throw new CrewException(CrewErrorCode.FAIL_TO_MODIFY_CREW_COMMENT);
		}
		comment.setContent(commentReq.getContent());
		commentRepository.save(comment);
	}

	// 댓글 삭제
	public void deleteComment(long commentId) {
		long memberId = validateToken();
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(CrewErrorCode.COMMENT_NOT_FOUND));
		if (memberId != comment.getMemberId()) {
			throw new CrewException(CrewErrorCode.FAIL_TO_DELETE_CREW_COMMENT);
		}
		commentRepository.delete(comment);
	}

}
