package com.itways.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.itways.dtos.GeneralApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor

public class AspectApiResponse {
	@Autowired
	private HttpServletRequest request;

	// A list of paths to exclude from API response wrapping.
	private static final List<String> EXCLUDED_PATHS = List.of("/actuator", "/v3/api-docs", "/swagger-resources",
			"/swagger-ui", "/webjars");

	/**
	 * Pointcut that matches all public methods in a class annotated
	 * with @RestController.
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void allControllers() {
	}

	@Around("allControllers()")
	public Object wrapApiResponse(ProceedingJoinPoint joinPoint) throws Throwable {
		// Bypass wrapping for Swagger, Actuator, and other internal endpoints.
		String path = request.getRequestURI();
		if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
			return joinPoint.proceed();
		}

		Object result = joinPoint.proceed();

		// If the controller already returns a ResponseEntity...
		if (result instanceof ResponseEntity) {

			ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
			Object body = responseEntity.getBody();

			// ...and the body is ALREADY a GeneralApiResponse (e.g., from an exception
			// handler),
			// then do not wrap it again. Just return it as is.
			if (body instanceof GeneralApiResponse) {
				return result;
			}

			// Otherwise, wrap the existing body and return a new ResponseEntity with the
			// original status.
			return ResponseEntity.status(responseEntity.getStatusCode())
					.body(GeneralApiResponse.success((HttpStatus) responseEntity.getStatusCode(), "Success", body));
		}

		// If the controller returns a raw object, wrap it in our standard success
		// response
		// and return it inside a ResponseEntity with a 200 OK status.
		return ResponseEntity.ok(GeneralApiResponse.success(result));

	}

}
