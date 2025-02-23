package com.luv2code.order.rest.handler.response;

public record ValidationErrorResponse(String object, String field, Object rejectedValue, String message) {

}
