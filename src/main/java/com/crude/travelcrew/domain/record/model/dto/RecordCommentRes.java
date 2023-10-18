package com.crude.travelcrew.domain.record.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.record.model.entity.RecordComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordCommentRes {

	private Long id;

	private String profileImgUrl;

	private double heartBeat;

	private int reportCnt;

	private String content;

	private LocalDateTime createdAt;

	public static RecordCommentRes fromEntity(RecordComment recordComment) {
		return RecordCommentRes.builder()
			.id(recordComment.getId())
			.profileImgUrl(recordComment.getMember().getProfileImgUrl())
			.heartBeat(recordComment.getMember().getMemberProfile().getHeartBeat())
			.reportCnt(recordComment.getMember().getMemberProfile().getReportCnt())
			.content(recordComment.getContent())
			.createdAt(recordComment.getCreatedAt())
			.build();
	}
}
