package com.playtomic.tests.wallet.exception.handler;

import com.playtomic.tests.wallet.exception.PaymentServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
@Component
public class WalletExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = PaymentServiceException.class)
    protected ResponseEntity<Object> handlePaymentException(PaymentServiceException ex, WebRequest request) {
        String bodyOfResponse = "There was an error operating the payment service";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
