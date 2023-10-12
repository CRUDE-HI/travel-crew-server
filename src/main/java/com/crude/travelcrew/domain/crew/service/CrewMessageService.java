package com.crude.travelcrew.domain.crew.service;

import com.crude.travelcrew.domain.crew.model.dto.ChatMessageReq;

public interface CrewMessageService {

	// 채팅방 입장 메세지
	void enter(ChatMessageReq request);

	// 채팅 메세지 전달
	void send(ChatMessageReq request);
}
