package com.crude.travelcrew.domain.crew.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.crew.model.entity.CrewComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CrewCommentRes {
	private long commentId;
	private long crewId;
	private long memberId;
	private String nickName;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String profileImgUrl;
	private double heartBeat;
	private int reportCnt;
	public static CrewCommentRes fromEntity(CrewComment crewComment) {
		return CrewCommentRes.builder()
			.commentId(crewComment.getCommentId())
			.crewId(crewComment.getCrewId())
			.memberId(crewComment.getMember().getId())
			.nickName(crewComment.getMember().getNickname())
			.content(crewComment.getContent())
			.createdAt(crewComment.getCreatedAt())
			.updatedAt(crewComment.getUpdatedAt())
			.profileImgUrl(crewComment.getMember().getProfileImgUrl())
			.heartBeat(crewComment.getMember().getMemberProfile().getHeartBeat())
			.reportCnt(crewComment.getMember().getMemberProfile().getReportCnt())
			.build();
	}
}