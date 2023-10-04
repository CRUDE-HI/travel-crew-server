package com.crude.travelcrew.domain.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.board.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByCrewId(long crewId, Pageable pageable);
}