package com.crude.travelcrew.domain.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crude.travelcrew.domain.board.entity.Posts;

public interface PostsRepository extends JpaRepository<Posts, Long> {
	@Query(
		"SELECT cp FROM crew_post cp WHERE cp.title LIKE %:keyword% or cp.crewContent LIKE %:keyword%"
	)
	List<Posts> findByKeyword(String keyword, Pageable pageable);
}
