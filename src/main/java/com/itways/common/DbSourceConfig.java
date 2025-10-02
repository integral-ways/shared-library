package com.itways.common;

import lombok.Data;

@Data
public class DbSourceConfig {
	private String url;
	private String username;
	private String password;
	private String driver;
}
