package com.playtomic.tests.wallet.exception.handler;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * When a future with no return type (void) throws an exception these exceptions
 * will not be propagated to the calling thread. This class handles those exceptions
 */
public class WalletAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    //TODO: Update handling
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

        System.out.println("Exception message - " + throwable.getMessage());
        System.out.println("Method name - " + method.getName());
        for (Object param : obj) {
            System.out.println("Parameter value - " + param);
        }
    }

}