package com.crude.travelcrew.domain.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.board.dto.CommentReq;
import com.crude.travelcrew.domain.board.dto.CommentRes;
import com.crude.travelcrew.domain.board.dto.PostListRes;
import com.crude.travelcrew.domain.board.dto.PostsReq;
import com.crude.travelcrew.domain.board.dto.PostsRes;
import com.crude.travelcrew.domain.board.service.PostsService;

@RestController
@RequestMapping("/api/crew")
public class PostsController {

	private final PostsService postsService;

	public PostsController(PostsService postsService) {
		this.postsService = postsService;
	}

	// 글 조회
	@GetMapping
	public ResponseEntity<List<PostListRes>> listAllCrew(
		@RequestParam(value = "keyword", defaultValue = "") String keyword,
		Pageable pageable) {
		return ResponseEntity.ok(postsService.listPosts(keyword, pageable));
	}

	// 글 등록
	@PostMapping("/post")
	public PostsRes createCrew(@RequestBody PostsReq requestDto) {
		PostsRes posts = postsService.createCrew(requestDto);
		return posts;
	}

	// 글 수정
	@PutMapping("/{crewId}")
	public Long updateCrew(@PathVariable Long crewId, @RequestBody PostsReq requestDto) {
		return postsService.updateCrew(crewId, requestDto);
	}

	// 글 삭제
	@DeleteMapping("/{crewId}")
	public Long deleteCrew(@PathVariable Long crewId) {
		return postsService.deleteCrew(crewId);
	}

	// 댓글 조회
	@GetMapping("/{crewId}/comment")
	public ResponseEntity<List<CommentRes>> listComment(@PathVariable long crewId, Pageable pageable) {
		return ResponseEntity.ok(postsService.listComment(crewId, pageable));
	}

	// 댓글 등록
	@PostMapping("/{crewId}/comment")
	public ResponseEntity<Object> createComment(@PathVariable long crewId, @Valid CommentReq commentReq) {
		postsService.createComment(crewId, commentReq);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// 댓글 수정
	@PatchMapping("/{crewId}/comment/{commentId}")
	public ResponseEntity<Object> modifyComment(@PathVariable long commentId, @Valid CommentReq commentReq) {
		postsService.modifyComment(commentId, commentReq);
		return ResponseEntity.ok().build();
	}

	// 댓글 삭제
	@DeleteMapping("/{crewId}/comment/{commentId}")
	public ResponseEntity<Object> deleteComment(@PathVariable long commentId) {
		postsService.deleteComment(commentId);
		return ResponseEntity.ok().build();
	}

}
