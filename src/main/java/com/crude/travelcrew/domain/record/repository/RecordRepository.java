package com.crude.travelcrew.domain.record.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crude.travelcrew.domain.record.model.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

	@Query(
		"SELECT cp FROM Record cp WHERE cp.title LIKE %:keyword% or cp.content LIKE %:keyword%"
	)
	List<Record> findByKeyword(String keyword, Pageable pageable);
}
