package com.crude.travelcrew.domain.crew.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import com.crude.travelcrew.global.entity.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrewMessage extends BaseTime {

	@Id
	@Column(name = "crew_message_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "crew_member_id", referencedColumnName = "crew_member_id_column_name_in_crew_member"),
		@JoinColumn(name = "crew_id", referencedColumnName = "crew_id_column_name_in_crew_member")
	})
	private CrewMember crewMember;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String message;

}
