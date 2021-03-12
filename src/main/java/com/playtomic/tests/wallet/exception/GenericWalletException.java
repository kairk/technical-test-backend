package com.playtomic.tests.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericWalletException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public GenericWalletException(String message, HttpStatus status) {
        super(message);

        this.message = message;
        this.status = status;
    }

    public GenericWalletException(String message, Throwable ex, HttpStatus status) {
        super(message, ex);

        this.message = message;
        this.status = status;
    }

    public GenericWalletException(String message) {
        super(message);

        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GenericWalletException(String message, Throwable ex) {
        super(message, ex);

        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
