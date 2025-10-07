package com.itways.common.errors;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(name = "itways.error-codes.disable", havingValue = "true")
public class LocalErrorsCodeFinder implements ErrorsCodeFinder {

	@Autowired
	private MessageSource messageSource;

	public MessageCode getMessage(String code, Object[] args, Locale locale) {
		String msg = messageSource.getMessage(code, args, code, locale);

		return MessageCode.builder().code(code).title(null).message(msg).icon(null).build();
	}

}
