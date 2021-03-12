package com.playtomic.tests.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ParseAmountException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public ParseAmountException(String message, HttpStatus status) {
        super(message);

        this.message = message;
        this.status = status;
    }

    public ParseAmountException(String message, Throwable ex, HttpStatus status) {
        super(message, ex);

        this.message = message;
        this.status = status;
    }

    public ParseAmountException(String message) {
        super(message);

        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ParseAmountException(String message, Throwable ex) {
        super(message, ex);

        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
