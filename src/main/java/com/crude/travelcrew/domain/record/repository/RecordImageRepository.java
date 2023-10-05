package com.crude.travelcrew.domain.record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordImage;
import com.crude.travelcrew.domain.record.repository.custom.CustomRecordImageRepository;

public interface RecordImageRepository extends JpaRepository<RecordImage, Long>,
	CustomRecordImageRepository {

	List<RecordImage> findAllByRecord(Record record);
}
