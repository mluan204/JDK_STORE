package com.example.Backend_IE303.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ApiResponse {
    private Integer status;
    private String message;
}
