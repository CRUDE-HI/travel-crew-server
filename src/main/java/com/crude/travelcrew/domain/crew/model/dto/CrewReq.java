package com.crude.travelcrew.domain.crew.model.dto;

import java.time.LocalDate;


import com.crude.travelcrew.domain.crew.model.constants.CrewGender;
import com.crude.travelcrew.domain.crew.model.constants.CrewStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class CrewReq {

	private String title;
	private String thumbnailImgUrl;
	private CrewStatus crewStatus;
	private Integer maxCrew;
	private LocalDate travelStart;
	private LocalDate travelEnd;
	private Integer crewAge;
	private CrewGender crewGender;
	private LocalDate updateAt;

	//Plan
	private Double latitude;
	private Double longitude;
	private String crewContent;



}
