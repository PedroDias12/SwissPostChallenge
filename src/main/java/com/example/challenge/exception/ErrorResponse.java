package com.example.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private int errorCode;
    private String errorMessage;
}
