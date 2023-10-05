package com.crude.travelcrew.domain.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


import com.crude.travelcrew.domain.board.contents.CrewGender;
import com.crude.travelcrew.domain.board.contents.CrewStatus;
import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.member.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostsRes {

	private String title;
	private Long id;
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



	public PostsRes (Posts posts){
		this.title = posts.getTitle();
		this.id = posts.getMember().getId();
		this.thumbnailImgUrl = posts.getThumbnailImgUrl();
		this.crewStatus = posts.getCrewStatus();
		this.maxCrew = posts.getMaxCrew();
		this.travelStart = posts.getTravelStart();
		this.travelEnd = posts.getTravelEnd();
		this.crewAge = posts.getCrewAge();
		this.crewGender = posts.getCrewGender();
		this.updateAt = posts.getUpdateAt();

		this.latitude = posts.getLatitude();
		this.longitude = posts.getLongitude();
		this.crewContent = posts.getCrewContent();

	}

}
