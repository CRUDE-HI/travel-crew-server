package com.crude.travelcrew.domain.record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

	List<Record> findAllByMember(Member member);
}
