package com.payflow.paymentapi.advice;

import com.payflow.paymentapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleBadRequest(IllegalArgumentException ex) {
        return ApiResponse.error("BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGeneral(Exception ex) {
        return ApiResponse.error("ERROR", ex.getMessage());
    }
}
