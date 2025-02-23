package com.example.challenge.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class WalletConflictException extends ServiceException {

    public WalletConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
