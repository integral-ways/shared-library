package com.itways.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import com.itways.utility.SyncData;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
@Endpoint(id = SyncData.CACHE)
public class CacheRefreshEndpoint {

	private final InMemoryCacheProvider cacheProvider;

	/** ✅ List all caches with item count */
	@ReadOperation
	public List<CacheInfo> list() {
		List<CacheInfo> caches = new ArrayList<>();
		Map<String, Map<String, InMemoryCacheProvider.CacheEntry>> allCaches = cacheProvider.getAllCaches();

		for (var entry : allCaches.entrySet()) {
			String cacheName = entry.getKey();
			long size = entry.getValue().size();
			caches.add(new CacheInfo(cacheName, size));
		}
		return caches;
	}

	/** ✅ Clear a specific cache entirely */
	@DeleteOperation
	public String clearCache(@Selector String cacheName) {
		cacheProvider.clear(cacheName);
		log.info("✅ Cleared cache: {}", cacheName);
		return "Cache '" + cacheName + "' cleared successfully.";
	}

	/** ✅ Clear specific key from a cache */
	@DeleteOperation
	public String clearCacheKey(@Selector String cacheName, @Selector String key) {
		cacheProvider.evict(cacheName, key);
		log.info("✅ Evicted key '{}' from cache '{}'", key, cacheName);
		return "Key '" + key + "' evicted from cache '" + cacheName + "'.";
	}
}