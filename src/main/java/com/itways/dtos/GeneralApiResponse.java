package com.itways.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itways.common.errors.ErrorMsgLocal;
import com.itways.common.errors.MessageCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralApiResponse<T> {

	private final Instant timestamp;
	private final int status;
	private final String message;
	private final T data;

	/**
	 * Creates a standard success response with a 200 OK status.
	 */
	public static <T> GeneralApiResponse<T> success(T data) {
		return new GeneralApiResponse<>(Instant.now(), HttpStatus.OK.value(), "Success", data);
	}

	/**
	 * Creates a success response with a custom message and status.
	 */
	public static <T> GeneralApiResponse<T> success(HttpStatus status, MessageCode error, T data) {
		return new GeneralApiResponse<>(Instant.now(), status.value(), error != null ? error.getMessage() : "Success", data);
	}

	/**
	 * Creates a standard error response with a MessageError payload.
	 */
	public static GeneralApiResponse<MessageCode> error(HttpStatus status, MessageCode error) {
		return new GeneralApiResponse<>(Instant.now(), status.value(), error != null ? error.getMessage() : "Error",
				error);
	}

	/**
	 * Creates an error response with a MessageError message and extra data (e.g.
	 * validation errors).
	 */
	public static <T> GeneralApiResponse<T> error(HttpStatus status, MessageCode error, T data) {
		return new GeneralApiResponse<>(Instant.now(), status.value(), error != null ? error.getMessage() : "Error",
				data);
	}
}
