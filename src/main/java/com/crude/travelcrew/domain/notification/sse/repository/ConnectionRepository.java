package com.crude.travelcrew.domain.notification.sse.repository;

public interface ConnectionRepository<T, R> {

	void add(T key, R connection);

	R get(T key);

	void delete(R connection);
}
