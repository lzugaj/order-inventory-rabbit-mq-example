package com.luv2code.order.rest.handler.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp,
        int httpStatusCode,
        HttpStatus httpStatus,
        String message,
        String messageKey,
        String path,
        List<ValidationErrorResponse> validationErrorResponses
) {

    public ApiErrorResponse(
            LocalDateTime timestamp,
            HttpStatus httpStatus,
            String message,
            String messageKey,
            String path
    ) {
        this(
                timestamp,
                httpStatus.value(),
                httpStatus,
                message,
                messageKey,
                path,
                null
        );
    }

    public ApiErrorResponse(
            LocalDateTime timestamp,
            HttpStatus httpStatus,
            String message,
            String messageKey,
            String path,
            List<ValidationErrorResponse> validationErrorResponses
    ) {
        this(
                timestamp,
                httpStatus.value(),
                httpStatus,
                message,
                messageKey,
                path,
                validationErrorResponses
        );
    }
}