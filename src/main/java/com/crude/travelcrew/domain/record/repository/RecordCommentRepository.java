package com.crude.travelcrew.domain.record.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordComment;
import com.crude.travelcrew.domain.record.repository.custom.CustomRecordCommentRepository;

public interface RecordCommentRepository extends JpaRepository<RecordComment, Long>,
	CustomRecordCommentRepository {

	Optional<RecordComment> findByRecordAndId(Record record, Long recordCommentId);

	List<RecordComment> findByRecord(Record record, Pageable pageable);
}
