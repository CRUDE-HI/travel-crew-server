package com.crude.travelcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.member.entity.Member;

public interface CrewRepository extends JpaRepository<Crew, Long> {
	@Query(
		"SELECT cp FROM crew cp WHERE cp.title LIKE %:keyword% or cp.crewContent LIKE %:keyword%"
	)
	List<Crew> findByKeyword(String keyword, Pageable pageable);

	List<Crew> findAllByMember(Member member);
}
