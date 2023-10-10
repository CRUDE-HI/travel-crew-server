package com.crude.travelcrew.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crude.travelcrew.domain.member.model.entity.MemberProfile;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
	@Modifying
	@Query(value = "UPDATE MEMBER_PROFILE\n"
		+ "SET MEMBER_PROFILE.heart_beat = (\n"
		+ "  SELECT 60 + (CASE WHEN SUM(val) > 120 THEN 120 WHEN SUM(val) < -60 THEN -60 ELSE SUM(val) END) AS val\n"
		+ "  FROM (\n"
		+ "    SELECT AVG(rating) AS val\n"
		+ "    FROM HEART_BEAT\n"
		+ "    WHERE HEART_BEAT.member_id = :memberId\n"
		+ "    GROUP BY member_id, crew_id\n"
		+ "  ) TAB\n"
		+ ")\n"
		+ "WHERE MEMBER_PROFILE.member_id = :memberId", nativeQuery = true)
	void updateHeartBeatByMember(@Param("memberId") Long memberId);
}
