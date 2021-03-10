package com.playtomic.tests.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WalletNotFoundException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public WalletNotFoundException(String message, HttpStatus status) {
        super(message);

        this.message = message;
        this.status = status;
    }

    public WalletNotFoundException(String message, Throwable ex, HttpStatus status) {
        super(message, ex);

        this.message = message;
        this.status = status;
    }

    public WalletNotFoundException(String message) {
        super(message);

        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }
}
