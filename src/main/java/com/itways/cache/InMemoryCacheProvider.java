package com.itways.cache;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryCacheProvider implements CacheProvider {

	public InMemoryCacheProvider() {
		log.info("✅ InMemoryCacheProvider initialized");
	}

	private static class CacheEntry {
		Object value;
		Instant expiresAt;

		CacheEntry(Object value, Instant expiresAt) {
			this.value = value;
			this.expiresAt = expiresAt;
		}
	}

	private final Map<String, Map<String, CacheEntry>> caches = new ConcurrentHashMap<>();

	@Override
	public <T> Optional<T> get(String cacheName, String key, Class<T> type) {
		Map<String, CacheEntry> cache = caches.get(cacheName);
		if (cache == null)
			return Optional.empty();

		CacheEntry entry = cache.get(key);
		if (entry == null)
			return Optional.empty();

		if (entry.expiresAt.isBefore(Instant.now())) {
			cache.remove(key);
			return Optional.empty();
		}
		return Optional.of(type.cast(entry.value));
	}

	@Override
	public void put(String cacheName, String key, Object value, long ttlSeconds) {
		Map<String, CacheEntry> cache = caches.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());
		Instant expiresAt = Instant.now().plusSeconds(ttlSeconds);
		cache.put(key, new CacheEntry(value, expiresAt));
	}

	@Override
	public void evict(String cacheName, String key) {
		Map<String, CacheEntry> cache = caches.get(cacheName);
		if (cache != null) {
			cache.remove(key);
		}
	}

	@Override
	public void clear(String cacheName) {
		caches.remove(cacheName);
	}
}