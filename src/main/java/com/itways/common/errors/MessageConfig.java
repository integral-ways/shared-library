package com.itways.common.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {

	@Bean
	MessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("error-messages/message"); // or "classpath:messages"
		ms.setDefaultEncoding("UTF-8");
		ms.setUseCodeAsDefaultMessage(true);
//		ms.setCacheSeconds(10);
		return ms;
	}

}
