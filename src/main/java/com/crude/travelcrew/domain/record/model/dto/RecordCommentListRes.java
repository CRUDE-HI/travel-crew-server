package com.crude.travelcrew.domain.record.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.crude.travelcrew.domain.record.model.entity.RecordComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordCommentListRes {

	private List<RecordCommentRes> comments;

	public static RecordCommentListRes fromEntityList(List<RecordComment> comments) {
		List<RecordCommentRes> responseList = comments.stream()
			.map(RecordCommentRes::fromEntity)
			.collect(Collectors.toList());

		return RecordCommentListRes.builder()
			.comments(responseList)
			.build();
	}
}