package com.crude.travelcrew.domain.member.entity;

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

import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.member.constants.MemberRole;
import com.crude.travelcrew.domain.member.constants.MemberStatus;
import com.crude.travelcrew.domain.member.constants.ProviderType;
import com.crude.travelcrew.domain.member.dto.SignUpReq;
import com.crude.travelcrew.global.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor
@ToString
public class Member extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = 30, unique = true)
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

	// 동행 게시판 참가자
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crew_id", updatable = false)
	private Posts posts;

}