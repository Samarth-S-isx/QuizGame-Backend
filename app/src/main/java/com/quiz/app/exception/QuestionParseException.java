package com.quiz.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class QuestionParseException extends RuntimeException {
    public QuestionParseException(String message) {
        super(message);
    }

    public QuestionParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
