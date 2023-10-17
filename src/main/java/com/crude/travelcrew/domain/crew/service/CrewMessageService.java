package com.crude.travelcrew.domain.crew.service;

import com.crude.travelcrew.domain.crew.model.dto.CrewMessageReq;

public interface CrewMessageService {

	// 채팅 메세지 전달
	void send(CrewMessageReq request, Long crewId);
}
