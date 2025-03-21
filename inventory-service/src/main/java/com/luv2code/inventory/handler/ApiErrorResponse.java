package com.luv2code.inventory.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp,
        int httpStatusCode,
        HttpStatus httpStatus,
        String message,
        String messageKey,
        String path
) {

    public ApiErrorResponse(
            LocalDateTime timestamp,
            HttpStatus httpStatus,
            String message,
            String messageKey,
            String path
    ) {
        this(timestamp, httpStatus.value(), httpStatus, message, messageKey, path);
    }

    public ApiErrorResponse(
            LocalDateTime timestamp,
            int httpStatusCode,
            HttpStatus httpStatus,
            String message,
            String messageKey,
            String path
    ) {
        this.timestamp = timestamp;
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
        this.messageKey = messageKey;
        this.path = path;
    }
}