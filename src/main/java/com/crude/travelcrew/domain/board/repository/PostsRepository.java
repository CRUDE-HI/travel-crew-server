package com.crude.travelcrew.domain.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.board.dto.PostListRes;
import com.crude.travelcrew.domain.board.entity.Posts;

public interface PostsRepository extends JpaRepository<Posts, Long> {
	List<PostListRes> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
