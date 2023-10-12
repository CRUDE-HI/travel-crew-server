package com.crude.travelcrew.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.crew.model.entity.CrewMessage;

public interface CrewMessageRepository extends JpaRepository<CrewMessage, Long> {
}
