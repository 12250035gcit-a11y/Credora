package com.credora.backend.common;

import org.springframework.http.HttpStatus;

/**
 * A single exception type for every "expected" error (bad login, job not found, etc.)
 * so the controller layer doesn't need try/catch everywhere - the GlobalExceptionHandler
 * turns this into a clean JSON error response.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
