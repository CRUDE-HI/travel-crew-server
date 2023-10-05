package com.crude.travelcrew.domain.crew.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.crude.travelcrew.domain.member.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrewScrap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crew_crap_id")
	private Long id;

	@JoinColumn(name = "crew_id", updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Crew crew;

	@JoinColumn(name = "member_id", updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
}
