package com.crude.travelcrew.domain.travelrecord.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.travelrecord.model.entity.Record;
import com.crude.travelcrew.domain.travelrecord.model.entity.RecordComment;
import com.crude.travelcrew.domain.travelrecord.repository.custom.CustomRecordCommentRepository;

public interface RecordCommentRepository extends JpaRepository<RecordComment, Long>,
	CustomRecordCommentRepository {

	Optional<RecordComment> findByRecordAndAndId(Record record, Long recordCommentId);
}
