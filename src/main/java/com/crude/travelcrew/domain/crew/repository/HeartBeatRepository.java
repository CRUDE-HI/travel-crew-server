package com.crude.travelcrew.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.HeartBeat;
import com.crude.travelcrew.domain.member.model.entity.Member;

public interface HeartBeatRepository extends JpaRepository<HeartBeat, Long> {
	boolean existsByCrewAndRater(Crew crew, Member rater);
}
