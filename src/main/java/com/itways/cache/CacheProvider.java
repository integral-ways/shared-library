package com.itways.cache;

import java.util.Optional;

public interface CacheProvider {
	<T> Optional<T> get(String cacheName, String key, Class<T> type);

	void put(String cacheName, String key, Object value, long ttlSeconds);

	void evict(String cacheName, String key);

	void clear(String cacheName);
}
