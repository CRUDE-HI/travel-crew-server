package com.crude.travelcrew.domain.crew.model.dto;

import java.time.LocalDate;

import com.crude.travelcrew.domain.crew.model.constants.CrewStatus;
import com.crude.travelcrew.domain.crew.model.entity.Crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CrewListRes {

	private Long crewId;
	private String title;
	private String crewContent;
	private String memberName;
	private String memberEmail;
	private String thumbnailImgUrl;
	private Integer maxCrew;
	private CrewStatus crewStatus;
	private LocalDate travelStart;
	private LocalDate travelEnd;

	public static CrewListRes getEntity(Crew post) {
		return CrewListRes.builder()
			.crewId(post.getCrewId())
			.title(post.getTitle())
			.crewContent(post.getCrewContent())
			.memberName(post.getMember().getNickname())
			.memberEmail(post.getMember().getEmail())
			.thumbnailImgUrl(post.getThumbnailImgUrl())
			.maxCrew(post.getMaxCrew())
			.crewStatus(post.getCrewStatus())
			.travelStart(post.getTravelStart())
			.travelEnd(post.getTravelEnd())
			.build();
	}
}
