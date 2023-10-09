package com.crude.travelcrew.domain.administer.dto.getCrew;

import java.time.LocalDate;

import com.crude.travelcrew.domain.crew.model.constants.CrewStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ADGetCrewRes {

	private Long crewId;
	private String title;

	private String nickName;

	private String thumbnailImgUrl;

	private CrewStatus crewStatus;

	private Integer maxCrew;

	private LocalDate travelStart;
	private LocalDate travelEnd;

	private Double latitude;
	private Double longitude;

	private String crewContent;
}
