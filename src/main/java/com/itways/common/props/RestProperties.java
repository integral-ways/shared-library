package com.itways.common.props;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("itways.rest")
@RefreshScope
@Data
public class RestProperties {
	private long timeout;
	private boolean offline;
	private String offlineDirectory;
	private Map<String, String> endpoints = new HashMap<String, String>();

	public String getEndpoint(String key) {
		return getEndpoints().get(key);
	}
}
