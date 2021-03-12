package com.playtomic.tests.wallet.business.impl;

import com.playtomic.tests.wallet.business.WalletBusinessLayer;
import com.playtomic.tests.wallet.exception.*;
import com.playtomic.tests.wallet.mapper.BigDecimalMapper;
import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.api.WalletResponse;
import com.playtomic.tests.wallet.model.service.Wallet;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

/**
 * Business layer that handles calls among services and data transformations
 */
@Component
public class WalletBusinessLayerDefault implements WalletBusinessLayer {

    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final BigDecimalMapper bdMapper;
    private final PaymentService paymentService;

    @Autowired
    public WalletBusinessLayerDefault(WalletService walletService, WalletMapper walletMapper, BigDecimalMapper bdMapper
            , PaymentService paymentService) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.bdMapper = bdMapper;
        this.paymentService = paymentService;
    }


    /**
     * Find asynchronously a wallet by its ID.
     *
     * @throws WalletNotFoundException if the wallet is not found
     */
    @Override
    @Async
    public CompletableFuture<WalletResponse> getWalletById(Long walletId) {
        Wallet serviceCall = walletService.getWalletById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Unable to find wallet with id: " + walletId));

        return CompletableFuture.completedFuture(serviceCall)
                .thenApply(walletMapper::serviceToRestResponse);
    }

    /**
     * Asynchronously charges a parsed amount to a wallet by its ID
     *
     * @throws ParseAmountException    if the amount is not a number
     * @throws WalletNotFoundException when there is a problem updating the wallet
     */
    @Override
    @Async
    public CompletableFuture<WalletResponse> chargeWalletById(Long walletId, String amount) {
        Wallet serviceCall;
        BigDecimal parsedAmount = bdMapper.stringToBigDecimal(amount)
                .orElseThrow(() -> new ParseAmountException("Unable to parse amount: " + amount));

        try {
            serviceCall = walletService.chargeWalletById(walletId, parsedAmount)
                    .orElseThrow(() -> new WalletNotFoundException("Unable to find wallet with id: " + walletId));
        } catch (WalletServiceException e) {
            throw new GenericWalletException("There was a problem updating the wallet balance", e);
        }

        return CompletableFuture.completedFuture(serviceCall)
                .thenApply(walletMapper::serviceToRestResponse);
    }

    /**
     * Asynchronously recharges a parsed amount to a wallet by its ID using a PaymenService
     * PaymentService call is executed in another thread expecting long wait times due to network
     *
     * @throws GenericWalletException  when PaymentService or WalletService fail
     * @throws WalletNotFoundException when the recharged wallet does not exists in the system
     */
    @Override
    @Async
    public CompletableFuture<WalletResponse> rechargeWalletById(Long walletId, String amount) {
        BigDecimal parsedAmount = bdMapper.stringToBigDecimal(amount)
                .orElseThrow(() -> new ParseAmountException("Unable to parse amount: " + amount));

        // As I'm not specifying the executor it should use the provided by spring boot,
        // but I think I would be better to change the PaymentService signature to Future<Void> and add @Async to let spring manage it.
        // I just felt like the provided service was part of the exercise and didn't want to change it
        return CompletableFuture.runAsync(() -> chargeOnPaymentService(paymentService, parsedAmount))
                .thenApply(resultComputation -> persistChargeWallet(walletId, parsedAmount))
                .thenApply(walletMapper::serviceToRestResponse);
    }

    private void chargeOnPaymentService(PaymentService paymentService, BigDecimal parsedAmount) {
        try {
            paymentService.charge(parsedAmount);
        } catch (PaymentServiceException e) {
            throw new GenericWalletException("There was an error calling the payment service", e);
        }
    }

    // This method is simplified for the sake of the exercise. Data inconsistencies between external services and your own system can be a problem.
    // A retry strategy or insert if not exists operation can solve some of these issues, but it depends on the desired business logic
    private Wallet persistChargeWallet(Long walletId, BigDecimal parsedAmount) {
        try {
            return walletService.rechargeWalletById(walletId, parsedAmount)
                    .orElseThrow(() -> new WalletNotFoundException("Unable to find wallet with id: " + walletId, HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (WalletServiceException e) {
            throw new GenericWalletException("Error persisting charge wallet information", e);
        }
    }

}
