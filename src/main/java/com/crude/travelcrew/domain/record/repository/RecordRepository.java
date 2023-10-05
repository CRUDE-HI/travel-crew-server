package com.crude.travelcrew.domain.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.record.model.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
