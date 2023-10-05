package com.crude.travelcrew.domain.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.board.entity.CrewComment;

public interface CrewCommentRepository extends JpaRepository<CrewComment, Long> {
	List<CrewComment> findByCrewId(long crewId, Pageable pageable);
}