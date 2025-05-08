package com.example.Backend_IE303.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        return ResponseEntity.ok().body(new ApiResponse(101,exception.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handlingCustomException(CustomException exception){
        return ResponseEntity.ok().body(new ApiResponse(exception.getStatus(),exception.getMessage()));
    }
}
