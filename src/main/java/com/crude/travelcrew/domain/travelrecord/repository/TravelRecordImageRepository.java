package com.crude.travelcrew.domain.travelrecord.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordImage;
import com.crude.travelcrew.domain.travelrecord.repository.custom.CustomTravelRecordImageRepository;

public interface TravelRecordImageRepository extends JpaRepository<TravelRecordImage, Long>,
	CustomTravelRecordImageRepository {

	List<TravelRecordImage> findAllByTravelRecord(TravelRecord travelRecord);
}
