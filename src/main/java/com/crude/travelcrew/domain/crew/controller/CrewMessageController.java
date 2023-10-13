package com.crude.travelcrew.domain.crew.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.model.dto.CrewMessageReq;
import com.crude.travelcrew.domain.crew.service.CrewMessageService;
import com.crude.travelcrew.domain.crew.service.CrewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CrewMessageController {

	private final CrewService crewService;
	private final CrewMessageService crewMessageService;

	@MessageMapping("/crew.chat/{crewId}")
	public void handleChat(CrewMessageReq request, @DestinationVariable Long crewId) {
		log.info("{} send message to roomId {}", request.getSender(), crewId);
		crewMessageService.send(request, crewId);
	}

}
