package com.crude.travelcrew.domain.record.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordHeart;

public interface RecordHeartRepository extends JpaRepository<RecordHeart, Long> {

	boolean existsByRecordAndMember(Record record, Member member);

	Optional<RecordHeart> findByRecordAndMember(Record record, Member member);
}
