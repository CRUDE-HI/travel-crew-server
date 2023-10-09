package com.crude.travelcrew.domain.crew.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.LastModifiedDate;

import com.crude.travelcrew.domain.crew.model.constants.CrewPlace;
import com.crude.travelcrew.domain.crew.model.constants.CrewStatus;
import com.crude.travelcrew.domain.crew.model.dto.CrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.global.entity.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "crew")
@ToString
public class Crew extends BaseTime {

	@Id
	@Column(name = "crew_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long crewId;

	@Column(nullable = false, length = 30 )
	private String title;

	@ManyToOne
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	@Column
	private String thumbnailImgUrl;

	@Enumerated(EnumType.STRING)
	private CrewPlace crewPlace;

	@Enumerated(EnumType.STRING)
	private CrewStatus crewStatus;

	// 참가인원수
	@Column
	private Integer maxCrew;

	@Column
	private LocalDate travelStart;

	@Column
	private LocalDate travelEnd;

	@Column
	@LastModifiedDate
	private LocalDateTime updateAt;

	//plan 내용
	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private String crewContent;


	public Crew(CrewReq crewReq) {
		this();
		this.title = crewReq.getTitle();
		this.crewStatus = crewReq.getCrewStatus();
		this.thumbnailImgUrl = crewReq.getThumbnailImgUrl();
		this.crewPlace = crewReq.getCrewPlace();
		this.maxCrew = crewReq.getMaxCrew();
		this.travelStart = crewReq.getTravelStart();
		this.travelEnd = crewReq.getTravelEnd();

		this.latitude = crewReq.getLatitude();
		this.longitude = crewReq.getLongitude();
		this.crewContent = crewReq.getCrewContent();

	}

	public void update(CrewReq crewReq) {
		this.title = crewReq.getTitle();
		this.crewStatus = crewReq.getCrewStatus();
		this.thumbnailImgUrl = crewReq.getThumbnailImgUrl();
		this.crewPlace = crewReq.getCrewPlace();
		this.maxCrew = crewReq.getMaxCrew();
		this.travelStart = crewReq.getTravelStart();
		this.travelEnd = crewReq.getTravelEnd();

		this.latitude = crewReq.getLatitude();
		this.longitude = crewReq.getLongitude();
		this.crewContent = crewReq.getCrewContent();
	}



	public CrewRes toCrewDTO () {
		CrewRes toPostsDTO = new CrewRes();
		toPostsDTO.setMemberId(this.getMember().getId());
		toPostsDTO.setTitle(this.getTitle());
		toPostsDTO.setThumbnailImgUrl(this.getThumbnailImgUrl());
		toPostsDTO.setCrewStatus(this.getCrewStatus());
		toPostsDTO.setMaxCrew(this.getMaxCrew());
		toPostsDTO.setTravelStart(this.getTravelStart());
		toPostsDTO.setTravelEnd(this.getTravelEnd());
		toPostsDTO.setUpdateAt(this.getUpdateAt());
		toPostsDTO.setLatitude(this.getLatitude());
		toPostsDTO.setLongitude(this.getLongitude());
		toPostsDTO.setCrewContent(this.getCrewContent());
		return toPostsDTO;
	}




}
