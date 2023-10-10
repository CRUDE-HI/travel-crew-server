package com.crude.travelcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.CrewComment;
import com.crude.travelcrew.domain.crew.repository.custom.CustomCrewCommentRepository;

public interface CrewCommentRepository extends JpaRepository<CrewComment, Long>,
	CustomCrewCommentRepository {
	List<CrewComment> findByCrewId(long crewId, Pageable pageable);

	List<CrewComment> findAllByCrewId(Long crewId);
}