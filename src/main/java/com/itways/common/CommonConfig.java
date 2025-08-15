package com.itways.common;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.itways.common")
@PropertySource("classpath:application-common.properties")
public class CommonConfig {

}
