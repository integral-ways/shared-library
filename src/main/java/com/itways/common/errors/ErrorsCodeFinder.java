package com.itways.common.errors;

import java.util.Locale;

public interface ErrorsCodeFinder {

	public MessageCode getMessage(String code, Object[] args, Locale locale);
	

}
