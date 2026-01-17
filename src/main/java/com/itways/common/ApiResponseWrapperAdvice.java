package com.itways.common;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.itways.dtos.GeneralApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

	private static final List<String> EXCLUDED_PATH_PREFIXES = List.of("/actuator", "/v3/api-docs", "api-docs",
			"/swagger-resources", "/swagger-ui", "/webjars", "swagger");

	private final HttpServletRequest request;
	private final ObjectMapper objectMapper;

	public ApiResponseWrapperAdvice(HttpServletRequest request, ObjectMapper objectMapper) {
		this.request = request;
		this.objectMapper = objectMapper;
		log.info("✅ ApiResponseWrapperAdvice initialized");
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		String path = request.getRequestURI();
		if (EXCLUDED_PATH_PREFIXES.stream().anyMatch(path::contains)) {
			return false;
		}

		Class<?> returnClass = returnType.getParameterType();

		// Don't wrap if it's already an ApiResponse
		if (GeneralApiResponse.class.isAssignableFrom(returnClass)) {
			return false;
		}

		// Check if it's a ResponseEntity containing GeneralApiResponse
		if (ResponseEntity.class.isAssignableFrom(returnClass)) {
			java.lang.reflect.Type genericType = returnType.getGenericParameterType();
			if (genericType instanceof java.lang.reflect.ParameterizedType pt) {
				java.lang.reflect.Type[] args = pt.getActualTypeArguments();
				if (args.length > 0 && args[0] instanceof Class<?> bodyClass) {
					if (GeneralApiResponse.class.isAssignableFrom(bodyClass)) {
						return false;
					}
				}
			}
		}

		// Don't wrap binary data, images, or Resources
		if (byte[].class.equals(returnClass)
				|| org.springframework.core.io.Resource.class.isAssignableFrom(returnClass)) {
			return false;
		}

		return true;
	}

	@Override
	@Nullable
	public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverRequest,
			ServerHttpResponse serverResponse) {

		// If already wrapped, return as is
		if (body instanceof GeneralApiResponse) {
			return body;
		}

		HttpStatus status = HttpStatus.OK;
		if (serverResponse instanceof ServletServerHttpResponse servletResponse) {
			status = HttpStatus.valueOf(servletResponse.getServletResponse().getStatus());
		}

		// Wrap the raw response body in GeneralApiResponse
		GeneralApiResponse<Object> wrapped = GeneralApiResponse.success(status, null, body);

		// Handle String responses to prevent ClassCastException in
		// StringHttpMessageConverter
		if (body instanceof String || org.springframework.http.converter.StringHttpMessageConverter.class
				.isAssignableFrom(selectedConverterType)) {
			try {
				return objectMapper.writeValueAsString(wrapped);
			} catch (JsonProcessingException e) {
				log.error("Error wrapping String response", e);
				return body;
			}
		}

		return wrapped;
	}
}
