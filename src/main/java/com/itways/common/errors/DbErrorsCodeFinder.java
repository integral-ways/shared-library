package com.itways.common.errors;

import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(name = "itways.error-codes.disable", havingValue = "false", matchIfMissing = true)
public class DbErrorsCodeFinder implements InitializingBean, ErrorsCodeFinder {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ErrorService errorService;

	@Autowired
	private CacheManager cacheManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (Objects.nonNull(cacheManager))
			errorService.load(false);
	}

	public MessageCode getMessage(String code, Object[] args, Locale locale) {
		try {
			var data = errorService.load(false);

			// If DB/local cache empty → fallback to messageSource
			if (data == null || data.isEmpty()) {
				String msg = messageSource.getMessage(code, args, code, locale);
				return MessageCode.builder().code(code).title(null).detail(msg).icon(null).build();
			}

			ErrorMsgLocal errorMsg = data.get(code);

			if (errorMsg == null) {
				String msg = messageSource.getMessage(code, args, code, locale);
				return MessageCode.builder().code(code).title(null).detail(msg).icon(null).build();
			}

			String title;
			String message;

			if ("ar".equalsIgnoreCase(locale.getLanguage())) {
				title = errorMsg.getTitleAr();
				message = errorMsg.getAr();
			} else {
				title = errorMsg.getTitleEn();
				message = errorMsg.getEn();
			}

			return MessageCode.builder().code(code).title(StringUtils.defaultIfBlank(title, null))
					.detail(StringUtils.defaultIfBlank(message, code)).icon(errorMsg.getIcon()).build();

		} catch (Exception ex) {
			String msg = messageSource.getMessage(code, args, code, locale);
			return MessageCode.builder().code(code).title(null).detail(StringUtils.defaultIfBlank(msg, code))
					.icon(null).build();
		}
	}

}
