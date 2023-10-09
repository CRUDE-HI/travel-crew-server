package com.crude.travelcrew.domain.administer.dto.getRecord;

import java.time.LocalDateTime;
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
public class ADGetRecordRes {

	private Long recordId;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private List<String> imageUrls;

	private String email;
	private String nickname;
	private String profileImgUrl;
}
