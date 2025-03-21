package com.luv2code.inventory.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static java.util.logging.Level.SEVERE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class InventoryRestExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE_KEY = "default.error.message";
    private static final String DEFAULT_ERROR_MESSAGE = "Sorry, we encountered an unexpected issue while processing your request. Please try again later or contact support for assistance.";

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleDefaultException(Exception exception, HttpServletRequest httpServletRequest) {
        log.error("An error occurred: " + exception.getMessage(), exception, SEVERE);
        log.error("Original exception class: " + exception.getClass().getName(), exception, SEVERE);

        return new ApiErrorResponse(
                LocalDateTime.now(),
                INTERNAL_SERVER_ERROR,
                DEFAULT_ERROR_MESSAGE,
                DEFAULT_ERROR_MESSAGE_KEY,
                httpServletRequest.getRequestURI()
        );
    }
}