package com.crude.travelcrew.domain.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.board.dto.PostsReq;
import com.crude.travelcrew.domain.board.dto.PostsRes;
import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.board.repository.PostsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostsService {

	private final PostsRepository postsRepository;

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
			()->new IllegalArgumentException("해당 아이디가 존재하지 않습니다. ")
		);
		posts.update(requestDto);
		return posts.getCrewId();
	}

	//삭제
	public Long deleteCrew(Long crewId) {
		postsRepository.deleteById(crewId);
		return crewId;
	}
}
