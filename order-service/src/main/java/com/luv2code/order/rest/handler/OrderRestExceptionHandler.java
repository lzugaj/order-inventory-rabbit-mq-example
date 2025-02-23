package com.luv2code.order.rest.handler;

import com.luv2code.order.rest.handler.response.ApiErrorResponse;
import com.luv2code.order.rest.handler.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.SEVERE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class OrderRestExceptionHandler {

    private static final String VALIDATION_ERROR_MESSAGE_KEY = "validation.error.message";
    private static final String VALIDATION_ERROR_MESSAGE = "Validation failed for one or more fields.";
    private static final String DEFAULT_ERROR_MESSAGE_KEY = "default.error.message";
    private static final String DEFAULT_ERROR_MESSAGE = "Sorry, we encountered an unexpected issue while processing your request. Please try again later or contact support for assistance.";

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest) {
        List<ValidationErrorResponse> validationErrorResponses = getValidationErrorResponse(exception);
        return new ApiErrorResponse(
                LocalDateTime.now(),
                BAD_REQUEST,
                VALIDATION_ERROR_MESSAGE,
                VALIDATION_ERROR_MESSAGE_KEY,
                httpServletRequest.getRequestURI(),
                validationErrorResponses
        );
    }

    private static List<ValidationErrorResponse> getValidationErrorResponse(MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse> validationErrorResponses = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
                        error.getObjectName(),
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()
                );
                validationErrorResponses.add(validationErrorResponse);
            }
        });

        return validationErrorResponses;
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
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