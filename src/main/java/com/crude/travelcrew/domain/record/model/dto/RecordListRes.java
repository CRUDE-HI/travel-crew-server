package com.crude.travelcrew.domain.record.model.dto;

import java.time.LocalDateTime;

import com.crude.travelcrew.domain.record.model.entity.Record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordListRes {

	private Long recordId;
	private String memberNick;
	private String title;
	private String content;
	private Long heartsCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static RecordListRes getEntity(Record record) {
		return RecordListRes.builder()
			.recordId(record.getId())
			.memberNick(record.getMember().getNickname())
			.title(record.getTitle())
			.content(record.getContent())
			.createdAt(record.getCreatedAt())
			.updatedAt(record.getUpdatedAt())
			.build();
	}
}
