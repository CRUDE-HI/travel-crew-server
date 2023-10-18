package com.crude.travelcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface CrewRepository extends JpaRepository<Crew, Long> {
	@Query(
		"SELECT cp FROM crew cp WHERE cp.title LIKE %:keyword% or cp.crewContent LIKE %:keyword%"
	)
	Page<Crew> findByKeyword(String keyword, Pageable pageable);

	List<Crew> findAllByMember(Member member);

	@Query(value="SELECT *\n"
		+ "FROM CREW\n"
		+ "WHERE CREW_ID IN (SELECT CREW_ID FROM CREW_MEMBER WHERE MEMBER_ID = :id AND STATUS = 'APPROVED')\n"
		+ "AND CREW_ID NOT IN (SELECT CREW_ID FROM HEART_BEAT WHERE RATER = :id)\n"
		+ "AND TRAVEL_END <= now()", nativeQuery = true)
	List<Crew> findAllUnreviewed(@Param("id") Long id);
}
