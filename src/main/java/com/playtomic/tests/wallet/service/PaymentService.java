package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.custom.PaymentServiceException;

import java.math.BigDecimal;

public interface PaymentService {
    void charge(BigDecimal amount) throws PaymentServiceException;
}
