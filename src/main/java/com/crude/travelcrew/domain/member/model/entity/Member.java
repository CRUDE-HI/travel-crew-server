package com.crude.travelcrew.domain.member.model.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.member.model.constants.MemberRole;
import com.crude.travelcrew.domain.member.model.constants.MemberStatus;
import com.crude.travelcrew.domain.member.model.constants.ProviderType;
import com.crude.travelcrew.domain.member.model.dto.MemberRes;
import com.crude.travelcrew.domain.member.model.dto.SignUpReq;
import com.crude.travelcrew.global.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Member extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = 70, unique = true)
	private String email;

	private String password;

	private String nickname;

	private String profileImgUrl;

	@Enumerated(EnumType.STRING)
	private MemberStatus memberStatus;

	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
	@JsonManagedReference
	private MemberProfile memberProfile;

	public Member(SignUpReq signUpReq) {
		this();
		this.email = signUpReq.getEmail();
		this.password = signUpReq.getPassword();
		this.nickname = signUpReq.getNickname();
	}

	public Member(String nickname) {
		this.nickname = nickname;
	}

	public MemberRes toMemberDTO() {
		MemberRes toMemberDTO = new MemberRes();
		toMemberDTO.setEmail(this.getEmail());
		toMemberDTO.setNickname(this.getNickname());
		toMemberDTO.setProfileImgUrl(this.getProfileImgUrl());
		toMemberDTO.setMemberStatus(this.getMemberStatus());
		toMemberDTO.setProviderType(this.getProviderType());
		MemberProfile memberProfile = this.getMemberProfile();
		toMemberDTO.setBirth(memberProfile.getBirth());
		toMemberDTO.setGender(memberProfile.getGender());
		toMemberDTO.setHeartBeat(memberProfile.getHeartBeat());
		toMemberDTO.setReportCnt(memberProfile.getReportCnt());
		return toMemberDTO;
	}
}