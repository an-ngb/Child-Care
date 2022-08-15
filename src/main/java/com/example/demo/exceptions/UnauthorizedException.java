package com.example.demo.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class UnauthorizedException extends CommonException {

    public UnauthorizedException(String errorCode, String errorType, String message) {
        super(errorCode, errorType, message);
    }

    public UnauthorizedException(String errorCode, String message) {
        this.errorCode = "401 - ".concat(errorCode);
        this.errorType = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        this.message = message;
    }
}
