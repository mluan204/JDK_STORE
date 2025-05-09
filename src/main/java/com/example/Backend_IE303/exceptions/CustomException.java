package com.example.Backend_IE303.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CustomException extends RuntimeException {
    private int status;
    public CustomException(int status , String mess) {
        super(mess);
        status = status;
    }
}
