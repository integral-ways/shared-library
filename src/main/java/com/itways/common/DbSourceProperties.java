package com.itways.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties("itways.db")
@Component
public class DbSourceProperties {
	private Map<String, DbSourceConfig> configs = new HashMap<>();
}
