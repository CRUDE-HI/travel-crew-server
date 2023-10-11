package com.crude.travelcrew.domain.crew.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrewMember {

	@EmbeddedId
	private CrewMemberId id;

	private boolean isOwner;

	public CrewMember(Long memberId, Long crewId) {
		this.id = new CrewMemberId(memberId, crewId);
	}

}
