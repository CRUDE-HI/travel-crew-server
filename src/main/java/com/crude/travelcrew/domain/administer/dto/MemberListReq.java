package com.crude.travelcrew.domain.administer.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
public class MemberListReq {
	private int page;
	private int size;
	private String search;

	public Pageable pageable() {
		Sort.by("createdAt").descending();
		return PageRequest.of(page, size);
	}
}
