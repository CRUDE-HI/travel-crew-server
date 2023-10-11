package com.crude.travelcrew.domain.crew.model.dto;

import com.crude.travelcrew.domain.crew.model.constants.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReq {

	private MessageType messageType;
	private Long crewId;
	private String sender;
	private String content;

}
