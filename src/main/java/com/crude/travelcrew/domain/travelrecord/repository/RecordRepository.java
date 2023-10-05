package com.crude.travelcrew.domain.travelrecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.travelrecord.model.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
