package com.crude.travelcrew.domain.crew.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.global.entity.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HeartBeat extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "heart_beat_id")
	private Long id;

	@JoinColumn(name = "member_id", updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "crew_id", updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Crew crew;

	private Integer rating;

	@JoinColumn(name="rater", updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member rater;
}