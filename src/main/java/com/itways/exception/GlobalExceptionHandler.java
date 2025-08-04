package com.itways.exception;

import com.itways.dtos.GeneralApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, "An unexpected error occurred", locale);
    }

    // --- 400 - Bad Request ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, Locale locale) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error, locale);
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed for request: {}", errors);
        GeneralApiResponse<Map<String, String>> apiResponse = GeneralApiResponse.error(
                HttpStatus.BAD_REQUEST,
                getMessage("error.validation", locale),
                errors
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<GeneralApiResponse<Object>> handleApiException(ApiException ex, Locale locale) {
        String message = getMessage(ex.getMessage(), locale, ex.getArgs());
        log.warn("API Exception: {}", message);
        GeneralApiResponse<Object> apiResponse = GeneralApiResponse.error(HttpStatus.valueOf(400), message);
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(400));
    }



    // --- 400 - Bad Request ---
    public ResponseEntity<GeneralApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, Locale locale) {
        log.warn("Bad request: {}", ex.getMessage());
        GeneralApiResponse<Object> apiResponse = GeneralApiResponse.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(), locale
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    // --- 500 - Internal Server Error ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralApiResponse<Object>> handleGenericException(Exception ex, Locale locale) {
        // Use SLF4J for logging instead of ex.printStackTrace()
        log.error("An unexpected error occurred", ex);

        GeneralApiResponse<Object> apiResponse = GeneralApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getMessage("error.unexpected", locale)
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}