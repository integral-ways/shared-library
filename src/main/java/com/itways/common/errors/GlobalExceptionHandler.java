package com.itways.common.errors;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.itways.dtos.ApiException;
import com.itways.dtos.GeneralApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private ResourceBundleUtility resourceBundleUtility;

	public GlobalExceptionHandler() {
		log.info("✅ GlobalExceptionHandler initialized");
	}

	private MessageCode getMessage(String code, Locale locale, Object... args) {
		return resourceBundleUtility.getMessage(code, args, locale);
	}

	// --- 400 - Bad Request ---
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GeneralApiResponse<Map<String, MessageCode>>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex, Locale locale) {

		Map<String, MessageCode> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			String fieldName = error.getField();
			MessageCode messageError = getMessage("validation." + fieldName, locale, null);
			errors.put(fieldName, messageError);
		});

		log.warn("❌ Validation failed: {}", errors);

		MessageCode validationMessage = getMessage("error.validation", locale);
		GeneralApiResponse<Map<String, MessageCode>> apiResponse = GeneralApiResponse.error(HttpStatus.BAD_REQUEST,
				validationMessage, errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	// --- Custom business exceptions ---
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<GeneralApiResponse<MessageCode>> handleApiException(ApiException ex, Locale locale) {
		MessageCode messageError = getMessage(ex.getMessage(), locale, ex.getArgs());
		log.warn("⚠️ API Exception: {}", messageError);

		GeneralApiResponse<MessageCode> apiResponse = GeneralApiResponse.error(HttpStatus.BAD_REQUEST, messageError);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	// --- 400 - Illegal Argument ---
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<GeneralApiResponse<MessageCode>> handleIllegalArgumentException(IllegalArgumentException ex,
			Locale locale) {
		log.warn("⚠️ Bad request: {}", ex.getMessage());

		MessageCode messageError = getMessage(ex.getMessage(), locale);
		GeneralApiResponse<MessageCode> apiResponse = GeneralApiResponse.error(HttpStatus.BAD_REQUEST, messageError);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	// --- 502 - Remote Service Error ---
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<GeneralApiResponse<MessageCode>> handleFeignException(FeignException ex, Locale locale) {
		log.error("💥 Feign Exception occurred", ex);
		MessageCode messageCode ;
		try{
			String body = ex.contentUTF8();
			ObjectMapper objectMapper = new ObjectMapper();
			FeignErrorResponse error = objectMapper.readValue(body, FeignErrorResponse.class);
			messageCode = error.getMessage();

			log.warn("Feign error parsed : {}" , messageCode);
		}
		catch(Exception e){
			log.error("Feign error parsing failed", e);
			messageCode = getMessage("error.unexpected", locale);
		}
		GeneralApiResponse<MessageCode> apiResponse = GeneralApiResponse.error(HttpStatus.BAD_GATEWAY, messageCode);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiResponse);
	}


	// --- 500 - Internal Server Error ---
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GeneralApiResponse<MessageCode>> handleGenericException(Exception ex, Locale locale) {
		log.error("💥 Unexpected error occurred", ex);

		MessageCode messageError = getMessage("error.unexpected", locale);
		GeneralApiResponse<MessageCode> apiResponse = GeneralApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
				messageError);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	}
}
