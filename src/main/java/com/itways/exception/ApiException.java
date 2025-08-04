package com.itways.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String message;
    private final Object[] args;

    public ApiException(String message, Object... args) {
        super(message);
        this.message = message;
        this.args = args;
    }
}
