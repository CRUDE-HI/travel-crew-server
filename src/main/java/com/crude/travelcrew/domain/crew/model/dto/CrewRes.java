package com.crude.travelcrew.domain.crew.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


import com.crude.travelcrew.domain.crew.model.constants.CrewGender;
import com.crude.travelcrew.domain.crew.model.constants.CrewStatus;
import com.crude.travelcrew.domain.crew.model.entity.Crew;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CrewRes {

	private String title;
	private Long memberId;
	private String thumbnailImgUrl;
	private CrewStatus crewStatus;
	private Integer maxCrew;
	private LocalDate travelStart;
	private LocalDate travelEnd;
	private Integer crewAge;
	private CrewGender crewGender;

	private LocalDateTime updateAt;

	//plan 내용
	private Double latitude;
	private Double longitude;
	private String crewContent;



	public CrewRes(Crew crew){
		this.title = crew.getTitle();
		this.memberId = crew.getMember().getId();
		this.thumbnailImgUrl = crew.getThumbnailImgUrl();
		this.crewStatus = crew.getCrewStatus();
		this.maxCrew = crew.getMaxCrew();
		this.travelStart = crew.getTravelStart();
		this.travelEnd = crew.getTravelEnd();
		this.crewAge = crew.getCrewAge();
		this.crewGender = crew.getCrewGender();
		this.updateAt = crew.getUpdateAt();

		this.latitude = crew.getLatitude();
		this.longitude = crew.getLongitude();
		this.crewContent = crew.getCrewContent();

	}

}
