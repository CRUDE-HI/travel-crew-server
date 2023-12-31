package com.crude.travelcrew.domain.record.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.dto.MyRecordRes;
import com.crude.travelcrew.global.entity.BaseTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record extends BaseTime {

	@Id
	@Column(name = "record_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	@Builder.Default
	@OneToMany(mappedBy = "record")
	private List<RecordImage> recordImages = new ArrayList<>();

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public MyRecordRes toMyRecordDTO() {
		MyRecordRes toMyRecordDTO = new MyRecordRes();
		toMyRecordDTO.setRecordId(this.getId());
		toMyRecordDTO.setMemberNick(this.getMember().getNickname());
		toMyRecordDTO.setTitle(this.getTitle());
		toMyRecordDTO.setContent(this.getContent());
		toMyRecordDTO.setCreatedAt(this.getCreatedAt());
		toMyRecordDTO.setUpdatedAt(this.getUpdatedAt());
		return toMyRecordDTO;
	}

	public void blockContent() {
		this.title = "차단된 글입니다.";
		this.content = "차단된 글입니다.";
	}
}
