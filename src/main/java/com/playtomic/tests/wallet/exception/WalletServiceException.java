package com.playtomic.tests.wallet.exception;

public class WalletServiceException extends Exception {
    private final String message;

    public WalletServiceException(String message, Throwable ex) {
        super(message, ex);

        this.message = message;
    }

    public WalletServiceException(String message) {
        super(message);

        this.message = message;
    }
}
