package com.crude.travelcrew.domain.administer.dto.getRecord;

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
public class ADRecordListRes {
	private List<ADRecordListResponseDto> recordList;
	private int totalRecords;
	private int currentPage;
}
