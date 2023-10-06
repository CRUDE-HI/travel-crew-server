package com.crude.travelcrew.domain.administer.dto.getRecord;

import java.time.LocalDateTime;

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
public class ADRecordListResponseDto {

	private Long id;
	private String title;

	private String nickname;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
