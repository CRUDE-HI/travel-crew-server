package com.crude.travelcrew.domain.member.model.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.crude.travelcrew.domain.member.model.constants.Gender;
import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_profile")
public class MemberProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;

	private LocalDate birth;

	private String name;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private Double heartBeat;

	private int reportCnt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	@JsonBackReference
	private Member member;

	public MemberProfile(SignUpReq signUpReq) {
		this();
		this.birth = signUpReq.getBirth();
		this.name = signUpReq.getName();
		this.gender = signUpReq.getGender();
	}

	public void increaseReportCnt() {
		this.reportCnt += 1;
	}
}
