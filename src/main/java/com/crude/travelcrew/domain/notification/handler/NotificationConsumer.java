package com.crude.travelcrew.domain.notification.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crude.travelcrew.domain.notification.model.dto.Message;
import com.crude.travelcrew.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationService notificationService;

	@RabbitListener(queues = "notification.queue")
	public void consumeNotification(Message message) {
		log.info("rabbitmq consumes message >>> {}", message);
		notificationService.send(message);
	}
}

