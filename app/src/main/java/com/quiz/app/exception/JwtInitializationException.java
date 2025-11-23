package com.quiz.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JwtInitializationException extends RuntimeException {
    public JwtInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
