package com.itways.common.props;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@RefreshScope
@Data
@Configuration
@ConfigurationProperties(prefix = "itways.security")
public class SecurityProperties {

	private List<String> publicApis;
}
