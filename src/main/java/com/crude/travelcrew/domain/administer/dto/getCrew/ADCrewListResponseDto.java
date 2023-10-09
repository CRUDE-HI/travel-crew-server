package com.crude.travelcrew.domain.administer.dto.getCrew;

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
public class ADCrewListResponseDto {

	private Long id;
	private String title;

	private String nickName;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
