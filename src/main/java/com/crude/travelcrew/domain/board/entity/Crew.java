package com.crude.travelcrew.domain.board.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.LastModifiedDate;

import com.crude.travelcrew.domain.board.contents.CrewGender;
import com.crude.travelcrew.domain.board.contents.CrewStatus;
import com.crude.travelcrew.domain.board.dto.CrewReq;
import com.crude.travelcrew.domain.board.dto.CrewRes;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.global.entity.BaseTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	@Column
	private String thumbnailImgUrl;

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

	// 동행 참가자 명단
	@OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Member> memberList = new ArrayList<>();

	// 참가자 추가
	public void addMemberList(Member participant) {
		memberList.add(participant);
		participant.setCrew(this);
	}

	// 참가자 제거
	public void removeMemberList(Member participant) {
		memberList.remove(participant);
		participant.setCrew(null);
	}



	public Crew(CrewReq crewReq) {
		this();
		this.title = crewReq.getTitle();
		this.crewStatus = crewReq.getCrewStatus();
		this.thumbnailImgUrl = crewReq.getThumbnailImgUrl();
		this.maxCrew = crewReq.getMaxCrew();
		this.travelStart = crewReq.getTravelStart();
		this.travelEnd = crewReq.getTravelEnd();
		this.crewAge = crewReq.getCrewAge();
		this.crewGender = crewReq.getCrewGender();

		this.latitude = crewReq.getLatitude();
		this.longitude = crewReq.getLongitude();
		this.crewContent = crewReq.getCrewContent();

	}

	public void update(CrewReq crewReq) {
		this.title = crewReq.getTitle();
		this.crewStatus = crewReq.getCrewStatus();
		this.thumbnailImgUrl = crewReq.getThumbnailImgUrl();
		this.maxCrew = crewReq.getMaxCrew();
		this.travelStart = crewReq.getTravelStart();
		this.travelEnd = crewReq.getTravelEnd();
		this.crewAge = crewReq.getCrewAge();
		this.crewGender = crewReq.getCrewGender();

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
		toPostsDTO.setCrewAge(this.getCrewAge());
		toPostsDTO.setCrewGender(this.getCrewGender());
		toPostsDTO.setUpdateAt(this.getUpdateAt());
		toPostsDTO.setLatitude(this.getLatitude());
		toPostsDTO.setLongitude(this.getLongitude());
		toPostsDTO.setCrewContent(this.getCrewContent());
		return toPostsDTO;
	}




}
