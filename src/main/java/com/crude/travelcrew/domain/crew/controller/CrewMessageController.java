package com.crude.travelcrew.domain.crew.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crude.travelcrew.domain.crew.model.dto.CrewMessageReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewMessageRes;
import com.crude.travelcrew.domain.crew.service.CrewMessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CrewMessageController {

	private final CrewMessageService crewMessageService;

	private final static String CHAT_QUEUE_NAME = "chat.queue";

	@MessageMapping("/crew.chat/{crewId}")
	public void handleChat(CrewMessageReq request, @DestinationVariable Long crewId) {
		log.info("{} send message to roomId {}", request.getSender(), crewId);
		crewMessageService.send(request, crewId);
	}


	@RabbitListener(queues = CHAT_QUEUE_NAME)
	public void receive(CrewMessageRes response) {
		log.info("response.getContent() = {}", response.getContent());
	}
}
