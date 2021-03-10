package com.playtomic.tests.wallet.business.impl;

import com.playtomic.tests.wallet.business.WalletBusinessLayer;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.api.WalletResponse;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Business layer that handles calls among services and data transformations
 */
@Component
public class WalletBusinessLayerDefault implements WalletBusinessLayer {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletBusinessLayerDefault(WalletService walletService, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
    }


    /**
     * Find asynchronously a wallet by its ID.
     * @Throws WalletNotFoundException if the wallet is not found
     */
    @Override
    @Async
    public CompletableFuture<WalletResponse> getWalletById(Long walletId) {
        Optional<WalletResponse> serviceCall = walletService.getWalletById(walletId)
                .map(walletMapper::serviceToRestResponse);

        return CompletableFuture.completedFuture(serviceCall).
                thenApply(a -> a.orElseThrow(() -> new WalletNotFoundException("Unable to find wallet with id: " + walletId)));
    }
}
