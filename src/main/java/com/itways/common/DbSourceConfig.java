package com.itways.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DbSourceConfig {
	private String url;
	private String username;
	private String password;
	private String driver;
	
	private String ddl;
	private String showSql;
	private String formatSql;
}
