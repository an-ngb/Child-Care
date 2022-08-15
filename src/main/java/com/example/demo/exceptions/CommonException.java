package com.example.demo.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommonException extends RuntimeException {
    String errorCode;
    String errorType;
    String message;
    List<String> messages;

    public CommonException(String errorCode, String errorType, String message) {
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.message = message;
    }
}
