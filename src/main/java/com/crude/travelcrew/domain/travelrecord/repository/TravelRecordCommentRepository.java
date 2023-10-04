package com.crude.travelcrew.domain.travelrecord.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordComment;

public interface TravelRecordCommentRepository extends JpaRepository<TravelRecordComment, Long> {

	Optional<TravelRecordComment> findByTravelRecordAndAndId(TravelRecord travelRecord, Long travelRecordCommentId);
}
