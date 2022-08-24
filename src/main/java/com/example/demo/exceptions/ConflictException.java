package com.example.demo.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConflictException extends CommonException {
    public ConflictException(String errorCode, String errorType, String message) {
        super(errorCode, errorType, message);
    }

    public ConflictException(String errorCode, String message) {
        this.errorCode = "409 - ".concat(errorCode);
        this.errorType = "CONFLICT";
        this.message = message;
    }
}
