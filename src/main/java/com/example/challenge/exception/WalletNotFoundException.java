package com.example.challenge.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class WalletNotFoundException extends ServiceException {
    public WalletNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}