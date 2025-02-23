package com.example.challenge.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class CannotBuyAssetException extends ServiceException {
    public CannotBuyAssetException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
