package com.crude.travelcrew.domain.notification.sse;

import java.io.IOException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.crude.travelcrew.domain.notification.sse.repository.ConnectionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SseConnection {

	private final String key;

	private final SseEmitter sseEmitter;

	private final ConnectionRepository<String, SseConnection> connectionRepository;

	private final ObjectMapper objectMapper;

	public SseConnection(String key, ObjectMapper objectMapper,
		ConnectionRepository<String, SseConnection> connectionRepository) {
		this.key = key;
		this.sseEmitter = new SseEmitter(1000L * 60 * 60); // 1시간
		this.connectionRepository = connectionRepository;
		this.objectMapper = objectMapper;

		this.sseEmitter.onTimeout(() -> {
			log.info("on timeout");
			this.sseEmitter.complete();
			this.connectionRepository.delete(this);
		});

		this.sseEmitter.onCompletion(() -> {
			log.info("on completion");
			this.connectionRepository.delete(this);
		});

		this.sendMessage("INITIAL", "connect success");
	}

	public static SseConnection connect(String uniqueKey, ObjectMapper objectMapper,
		ConnectionRepository<String, SseConnection> connectionRepository) {
		return new SseConnection(uniqueKey, objectMapper, connectionRepository);
	}

	public void sendMessage(String eventName, Object data) {
		try {

			String json = this.objectMapper.writeValueAsString(data);
			SseEmitter.SseEventBuilder event = SseEmitter
				.event()
				.name(eventName)
				.data(json);

			this.sseEmitter.send(event);
		} catch (IOException e) {
			log.error(e.getMessage());
			this.sseEmitter.completeWithError(e);
		}
	}
}
