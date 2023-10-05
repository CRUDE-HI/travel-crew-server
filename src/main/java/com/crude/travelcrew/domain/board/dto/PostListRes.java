package com.crude.travelcrew.domain.board.dto;

import java.time.LocalDate;

import com.crude.travelcrew.domain.board.contents.CrewStatus;
import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostListRes {

	private Long crewId;
	private String title;
	private String crewContent;
	private Member member;
	private String thumbnailImgUrl;
	private Integer maxCrew;
	private CrewStatus crewStatus;
	private LocalDate travelStart;
	private LocalDate travelEnd;

	public static PostListRes getEntity(Posts post) {
		return PostListRes.builder()
			.crewId(post.getCrewId())
			.title(post.getTitle())
			.crewContent(post.getCrewContent())
			.member(post.getMember())
			.thumbnailImgUrl(post.getThumbnailImgUrl())
			.maxCrew(post.getMaxCrew())
			.crewStatus(post.getCrewStatus())
			.travelStart(post.getTravelStart())
			.travelEnd(post.getTravelEnd())
			.build();
	}
}
