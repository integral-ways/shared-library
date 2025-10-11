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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

	private static final List<String> EXCLUDED_PATH_PREFIXES = List.of("/actuator", "/v3/api-docs",
			"/swagger-resources", "/swagger-ui", "/webjars");

	private final HttpServletRequest request;

	public ApiResponseWrapperAdvice(HttpServletRequest request) {
		this.request = request;
		log.info("✅ ApiResponseWrapperAdvice initialized");
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		String path = request.getRequestURI();
		return !EXCLUDED_PATH_PREFIXES.stream().anyMatch(path::contains);
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

		// If body is ResponseEntity, unwrap and wrap inside GeneralApiResponse with
		// original status
		if (body instanceof ResponseEntity<?> responseEntity) {
			Object responseBody = responseEntity.getBody();

			if (responseBody instanceof GeneralApiResponse) {
				return body; // already wrapped
			}

			return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders())
					.body(GeneralApiResponse.success(HttpStatus.valueOf(responseEntity.getStatusCode().value()), null,
							responseBody));
		}

		// Wrap the raw response body in GeneralApiResponse with 200 OK
		return GeneralApiResponse.success(status,null,body);
	}
}
