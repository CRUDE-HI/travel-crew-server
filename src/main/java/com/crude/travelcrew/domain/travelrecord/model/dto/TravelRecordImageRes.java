package com.crude.travelcrew.domain.travelrecord.model.dto;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelRecordImageRes {

	private Long travelRecordImageId;

	private String imageUrl;

	public static TravelRecordImageRes fromEntity (TravelRecordImage image) {
		return TravelRecordImageRes.builder()
			.travelRecordImageId(image.getId())
			.imageUrl(image.getImageUrl())
			.build();
	}
}
