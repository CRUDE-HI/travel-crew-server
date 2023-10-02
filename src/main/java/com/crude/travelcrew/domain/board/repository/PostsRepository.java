package com.crude.travelcrew.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.board.entity.Posts;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
