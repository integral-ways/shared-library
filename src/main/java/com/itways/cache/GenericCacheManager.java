package com.itways.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

public class GenericCacheManager implements CacheManager {

	private final CacheProvider cacheProvider;
	private final long defaultTtlSeconds;

	private final ConcurrentHashMap<String, Cache> caches = new ConcurrentHashMap<>();

	public GenericCacheManager(CacheProvider cacheProvider, long defaultTtlSeconds) {
		this.cacheProvider = cacheProvider;
		this.defaultTtlSeconds = defaultTtlSeconds;
	}

	@Override
	@Nullable
	public Cache getCache(String name) {
		return caches.computeIfAbsent(name, cacheName -> new GenericCache(cacheName, cacheProvider, defaultTtlSeconds));
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(caches.keySet());
	}
}
