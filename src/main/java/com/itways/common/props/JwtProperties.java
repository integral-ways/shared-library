package com.itways.common.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
	private String secretKey;
	private String publicKey;
	private String privateKey;
	private long expirationTime;
}
