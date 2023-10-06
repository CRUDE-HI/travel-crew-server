package com.crude.travelcrew.domain.record.model.dto;

import com.crude.travelcrew.domain.record.model.entity.RecordImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordImageRes {

	private Long imageId;

	private String imageUrl;

	public static RecordImageRes fromEntity (RecordImage image) {
		return RecordImageRes.builder()
			.imageId(image.getId())
			.imageUrl(image.getImageUrl())
			.build();
	}
}
