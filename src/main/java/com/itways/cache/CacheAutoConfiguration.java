package com.itways.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.itways.cache")
public class CacheAutoConfiguration {

//	@Bean
//	public CacheProvider cacheProvider() {
//
//		log.info("✅ InMemoryCacheProvider initialized");
//		return new InMemoryCacheProvider();
//	}

	@Bean
	@Primary
	public CacheManager cacheManager(CacheProvider cacheProvider) {
		return new GenericCacheManager(cacheProvider, 3600); // default TTL 1 hour
	}
}
