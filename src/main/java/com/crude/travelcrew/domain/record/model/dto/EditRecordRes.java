package com.crude.travelcrew.domain.record.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.crude.travelcrew.domain.record.model.entity.Record;
import com.crude.travelcrew.domain.record.model.entity.RecordImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditRecordRes {

	private Long id;

	private String title;

	private String content;

	private List<RecordImageRes> images;

	private LocalDateTime createdAt;

	public static EditRecordRes fromEntity(Record record, List<RecordImage> images) {
		return EditRecordRes.builder()
			.id(record.getId())
			.title(record.getTitle())
			.content(record.getContent())
			.images(images.stream().map(RecordImageRes::fromEntity).collect(Collectors.toList()))
			.createdAt(record.getCreatedAt())
			.build();
	}

	public static EditRecordRes fromEntity(Record record) {
		return EditRecordRes.builder()
			.id(record.getId())
			.title(record.getTitle())
			.content(record.getContent())
			.images(new ArrayList<>())
			.createdAt(record.getCreatedAt())
			.build();
	}
}
