package com.itways.common.errors;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ResourceBundleUtility {

	@Autowired
	private ErrorsCodeFinder errorsCodeFinder;

	public MessageCode getMessage(String msgKey, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();

		if (StringUtils.isNotBlank(msgKey)) {
			return errorsCodeFinder.getMessage(msgKey, params, locale);
		} else {
			return MessageCode.builder().code(null).title(null).message(msgKey).icon(null).build();
		}
	}

	public MessageCode getMessage(String msgKey, Locale locale) {
		if (StringUtils.isNotBlank(msgKey)) {
			return errorsCodeFinder.getMessage(msgKey, null, locale);
		} else {
			return MessageCode.builder().code(null).title(null).message(msgKey).icon(null).build();
		}
	}

	public MessageCode getMessage(String msgKey, Object[] params, Locale locale) {
		return errorsCodeFinder.getMessage(msgKey, params, locale);
	}
}
