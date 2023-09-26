package com.crude.travelcrew.domain.member.entity;

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

import com.crude.travelcrew.domain.member.constants.Gender;
import com.crude.travelcrew.domain.member.dto.SignUpReq;

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
	private Member member;

	public MemberProfile(SignUpReq signUpReq) {
		this();
		this.birth = signUpReq.getBirth();
		this.name = signUpReq.getName();
		this.gender = signUpReq.getGender();
	}
}
