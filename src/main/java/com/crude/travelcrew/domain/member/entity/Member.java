package com.crude.travelcrew.domain.member.entity;

import com.crude.travelcrew.domain.member.constants.MemberRole;
import com.crude.travelcrew.domain.member.constants.MemberStatus;
import com.crude.travelcrew.domain.member.constants.ProviderType;
import com.crude.travelcrew.domain.member.dto.SignUpReq;
import com.crude.travelcrew.global.entity.Authority;
import com.crude.travelcrew.global.entity.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

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
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "member")
    private Set<Authority> authority;

    public Member(SignUpReq signUpReq) {
        this();
        this.email = signUpReq.getEmail();
        this.password = signUpReq.getPassword();
        this.nickname = signUpReq.getNickname();
    }

}