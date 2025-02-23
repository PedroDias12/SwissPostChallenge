package com.example.challenge.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AssetNotFoundException extends ServiceException {
    public AssetNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
