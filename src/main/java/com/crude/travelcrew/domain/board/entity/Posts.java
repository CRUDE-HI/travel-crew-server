package com.crude.travelcrew.domain.board.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.LastModifiedDate;

import com.crude.travelcrew.domain.board.contents.CrewGender;
import com.crude.travelcrew.domain.board.contents.CrewStatus;

public class Posts {

	@Id
	@Column(name = "crew_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long crewId;

	//글제목
	@Column(nullable = false, length = 30 )
	private String title;

	@Column
	private String thumbnailImgUrl;

	@Enumerated(EnumType.STRING)
	private CrewStatus crewStatus;

	@Column
	private Integer maxCrew;

	@Column
	private LocalDate travelStart;

	@Column
	private LocalDate travelEnd;

	@Column
	private Integer crewAge;

	@Enumerated(EnumType.STRING)
	private CrewGender crewGender;

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







}
