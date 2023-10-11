package com.crude.travelcrew.domain.record.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.crude.travelcrew.domain.member.model.entity.Member;

import com.crude.travelcrew.domain.record.model.dto.RecordHeartRes;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordHeart {

	@Id
	@Column(name = "record_heart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "record_id", updatable = false)
	private Record record;

	public RecordHeartRes toRecordHeartsDTO() {
		RecordHeartRes toDTO = new RecordHeartRes();
		toDTO.setRecord_heart_id(this.getId());
		toDTO.setMember(this.getMember());
		toDTO.setRecord(this.getRecord());
		return toDTO;
	}
}
