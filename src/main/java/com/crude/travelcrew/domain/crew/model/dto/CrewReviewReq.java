package com.crude.travelcrew.domain.crew.model.dto;

import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.HeartBeat;
import com.crude.travelcrew.domain.member.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CrewReviewReq {
	private long memberId;
	private Integer rating;

	public static HeartBeat toEntity(CrewReviewReq crewReviewReq, Crew crew, Member rater) {
		Member member = new Member();
		member.setId(crewReviewReq.getMemberId());
		return HeartBeat.builder()
			.member(member)
			.crew(crew)
			.rating(crewReviewReq.getRating())
			.rater(rater)
			.build();
	}

}