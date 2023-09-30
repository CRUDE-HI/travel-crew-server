package com.crude.travelcrew.domain.travelrecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;

public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {
}
