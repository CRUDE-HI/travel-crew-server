package com.crude.travelcrew.domain.travelrecord.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordHeart;

public interface TravelRecordHeartRepository extends JpaRepository<TravelRecordHeart, Long> {

	boolean existsByTravelRecordAndMember(TravelRecord travelRecord, Member member);

	Optional<TravelRecordHeart> findByTravelRecordAndMember(TravelRecord travelRecord, Member member);
}
