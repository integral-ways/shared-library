package com.itways.dtos;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itways.common.errors.MessageCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralApiResponse<T> {

	private Instant timestamp;
	private int status;
	private MessageCode message;
	private T data;

	/**
	 * Creates a standard success response with a 200 OK status.
	 */
	public static <T> GeneralApiResponse<T> success(T data) {
		return new GeneralApiResponse<>(Instant.now(), HttpStatus.OK.value(),
				MessageCode.builder().detail("Success").build(), data);
	}

	/**
	 * Creates a success response with a custom message and status.
	 */
	public static <T> GeneralApiResponse<T> success(HttpStatus status, MessageCode msg, T data) {
		return new GeneralApiResponse<>(Instant.now(), status.value(),
				msg != null ? msg : MessageCode.builder().detail("Success").build(), data);
	}

	/**
	 * Creates a standard error response with a MessageError payload.
	 */
	public static GeneralApiResponse<MessageCode> error(HttpStatus status, MessageCode msg) {
		return new GeneralApiResponse<>(Instant.now(), status.value(),
				msg != null ? msg : MessageCode.builder().detail("Error").build(), null);
	}

	/**
	 * Creates an error response with a MessageError message and extra data (e.g.
	 * validation errors).
	 */
	public static <T> GeneralApiResponse<T> error(HttpStatus status, MessageCode msg, T data) {
		return new GeneralApiResponse<>(Instant.now(), status.value(),
				msg != null ? msg : MessageCode.builder().detail("Error").build(), data);
	}
}
