package com.crude.travelcrew.domain.administer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecordListRes {
	private List<RecordListResponseDto> recordList;
	private int totalRecords;
	private int currentPage;
}
