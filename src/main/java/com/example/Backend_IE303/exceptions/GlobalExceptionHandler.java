package com.example.Backend_IE303.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiError> handlingRuntimeException(RuntimeException exception){
        return ResponseEntity.ok().body(new ApiError(100,exception.getMessage()));
    }
}
