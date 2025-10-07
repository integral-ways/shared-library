package com.itways.common.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ErrorsCodeConfiguration {

//	@Autowired
//	private CacheProperties cacheProperties;

	@Bean
	@Primary
	public MessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("error-messages/message");
		source.setUseCodeAsDefaultMessage(true);
		source.setDefaultEncoding("UTF-8");
		return source;
	}

//	@Bean
//	ErrorsCodeFinder errorsCodeFinder() {
////		ErrorsCodeFinder finder = cacheProperties.isDisableErrorCodes() ? new LocalErrorsCodeFinder()
////				: new DbErrorsCodeFinder();
//		return new DbErrorsCodeFinder();
//	}

}
