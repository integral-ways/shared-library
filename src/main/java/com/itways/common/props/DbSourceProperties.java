package com.itways.common.props;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@ConfigurationProperties("itways.db")
@Component
public class DbSourceProperties {
	private Map<String, DbSourceConfig> configs = new HashMap<>();

	@Slf4j
	@Data
	public static class DbSourceConfig {
		private String url;
		private String username;
		private String password;
		private String driver;

		private String ddl;
		private String showSql;
		private String formatSql;
	}

}
