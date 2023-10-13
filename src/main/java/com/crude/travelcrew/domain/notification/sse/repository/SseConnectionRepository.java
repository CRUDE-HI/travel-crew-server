package com.crude.travelcrew.domain.notification.sse.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.crude.travelcrew.domain.notification.sse.SseConnection;

@Component
public class SseConnectionRepository implements ConnectionRepository<String, SseConnection> {

	private static final Map<String, SseConnection> connections = new ConcurrentHashMap<>();

	@Override
	public void add(String key, SseConnection sseConnection) {
		connections.put(key, sseConnection);
	}

	@Override
	public SseConnection get(String key) {
		return connections.get(key);
	}

	@Override
	public void delete(SseConnection connection) {
		connections.remove(connection.getKey());
	}
}
