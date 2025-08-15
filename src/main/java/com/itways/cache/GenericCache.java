package com.itways.cache;

import java.util.Optional;
import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;

public class GenericCache implements Cache {

	private final String name;
	private final CacheProvider cacheProvider;
	private final long defaultTtlSeconds;

	public GenericCache(String name, CacheProvider cacheProvider, long defaultTtlSeconds) {
		this.name = name;
		this.cacheProvider = cacheProvider;
		this.defaultTtlSeconds = defaultTtlSeconds;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return cacheProvider;
	}

	@Override
	@Nullable
	public ValueWrapper get(Object key) {
		if (key == null)
			return null;

		Optional<?> value = cacheProvider.get(name, key.toString(), Object.class);
		return value.map(SimpleValueWrapper::new).orElse(null);
	}

	@Override
	@Nullable
	public <T> T get(Object key, Class<T> type) {
		if (key == null)
			return null;

		Optional<T> value = cacheProvider.get(name, key.toString(), type);
		return value.orElse(null);
	}

	@Override
	@Nullable
	public <T> T get(Object key, Callable<T> valueLoader) {
		T value = get(key, (Class<T>) Object.class);
		if (value != null) {
			return value;
		}
		try {
			value = valueLoader.call();
			put(key, value);
			return value;
		} catch (Exception e) {
			throw new ValueRetrievalException(key, valueLoader, e);
		}
	}

	@Override
	public void put(Object key, @Nullable Object value) {
		if (key == null || value == null)
			return;

		cacheProvider.put(name, key.toString(), value, defaultTtlSeconds);
	}

	@Override
	@Nullable
	public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
		ValueWrapper existing = get(key);
		if (existing == null) {
			put(key, value);
			return null;
		}
		return existing;
	}

	@Override
	public void evict(Object key) {
		if (key == null)
			return;

		cacheProvider.evict(name, key.toString());
	}

	@Override
	public void clear() {
		cacheProvider.clear(name);
	}
}