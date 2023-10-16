package com.crude.travelcrew.domain.notification.handler;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.crude.travelcrew.domain.notification.model.dto.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

	private static final String EXCHANGE_NAME = "notification.exchange";
	private static final String ROUTING_KEY = "notification.key";

	private final RabbitTemplate rabbitTemplate;

	public void produce(Message message) {
		log.info("rabbitmq produces message >>> {}", message);
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
	}
}

