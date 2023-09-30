package com.crude.travelcrew.domain.board.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.board.dto.PostsReq;
import com.crude.travelcrew.domain.board.dto.PostsRes;
import com.crude.travelcrew.domain.board.service.PostsService;

@RestController
@RequestMapping("/crew/")
public class PostsController {

	private final PostsService postsService;

	public PostsController(PostsService postsService) {
		this.postsService = postsService;
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
		return postsService.updateCrew(crewId,requestDto);
	}

	// 글 삭제
	@DeleteMapping("/{crewId}")
	public Long deleteCrew(@PathVariable Long crewId) {
		return postsService.deleteCrew(crewId);
	}

}
