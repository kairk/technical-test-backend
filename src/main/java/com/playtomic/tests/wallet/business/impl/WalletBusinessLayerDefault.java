package com.playtomic.tests.wallet.business.impl;

import com.playtomic.tests.wallet.business.WalletBusinessLayer;
import com.playtomic.tests.wallet.exception.ParseAmountException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.mapper.BigDecimalMapper;
import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.api.WalletResponse;
import com.playtomic.tests.wallet.model.service.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public WalletBusinessLayerDefault(WalletService walletService, WalletMapper walletMapper, BigDecimalMapper bdMapper) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.bdMapper = bdMapper;
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
        BigDecimal parsedAmount = bdMapper.stringToBigDecimal(amount)
                .orElseThrow(() -> new ParseAmountException("Unable to parse amount: " + amount));

        Wallet serviceCall = walletService.chargeWalletById(walletId, parsedAmount)
                .orElseThrow(() -> new WalletNotFoundException("Unable to find wallet with id: " + walletId));

        return CompletableFuture.completedFuture(serviceCall)
                .thenApply(walletMapper::serviceToRestResponse);
    }
}
