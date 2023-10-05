package com.crude.travelcrew.domain.travelrecord.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordComment;
import com.crude.travelcrew.domain.travelrecord.repository.custom.CustomTravelRecordCommentRepository;

public interface TravelRecordCommentRepository extends JpaRepository<TravelRecordComment, Long>,
	CustomTravelRecordCommentRepository {

	Optional<TravelRecordComment> findByTravelRecordAndAndId(TravelRecord travelRecord, Long travelRecordCommentId);
}
