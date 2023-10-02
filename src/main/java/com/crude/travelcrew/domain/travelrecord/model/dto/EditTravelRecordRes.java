package com.crude.travelcrew.domain.travelrecord.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecord;
import com.crude.travelcrew.domain.travelrecord.model.entity.TravelRecordImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditTravelRecordRes {

	private Long id;

	private String title;

	private String content;

	private List<TravelRecordImageRes> images;

	private LocalDateTime createdAt;

	public static EditTravelRecordRes fromEntity(TravelRecord travelRecord, List<TravelRecordImage> images) {
		return EditTravelRecordRes.builder()
			.id(travelRecord.getId())
			.title(travelRecord.getTitle())
			.content(travelRecord.getContent())
			.images(images.stream().map(TravelRecordImageRes::fromEntity).collect(Collectors.toList()))
			.createdAt(travelRecord.getCreatedAt())
			.build();
	}

	public static EditTravelRecordRes fromEntity(TravelRecord travelRecord) {
		return EditTravelRecordRes.builder()
			.id(travelRecord.getId())
			.title(travelRecord.getTitle())
			.content(travelRecord.getContent())
			.images(new ArrayList<>())
			.createdAt(travelRecord.getCreatedAt())
			.build();
	}
}
