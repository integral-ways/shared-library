package com.itways.common;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		// Base name without locale extension: it will load messages.properties and
		// messages_ar.properties automatically
		messageSource.setBasename("messages/messages");

		// Encoding for property files
		messageSource.setDefaultEncoding("UTF-8");

		// If message code not found, show code instead of throwing exception
		messageSource.setUseCodeAsDefaultMessage(true);

		// Cache duration in seconds (set to reload every 10 seconds, or -1 to cache
		// forever)
		messageSource.setCacheSeconds(-1);

		return messageSource;
	}
}
