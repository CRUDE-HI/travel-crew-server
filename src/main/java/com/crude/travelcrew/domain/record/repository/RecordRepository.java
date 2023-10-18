package com.crude.travelcrew.domain.record.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

	@Query(
		"SELECT cp FROM Record cp WHERE cp.title LIKE %:keyword% or cp.content LIKE %:keyword%"
	)
	Page<Record> findByKeyword(String keyword, Pageable pageable);

	List<Record> findAllByMember(Member member);

	@Query(value = "SELECT COUNT(*) FROM RECORD_HEART WHERE record_id = :recordId", nativeQuery = true)
	long countHeartsForRecord(Long recordId);
}
