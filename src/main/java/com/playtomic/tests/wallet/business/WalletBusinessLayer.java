package com.playtomic.tests.wallet.business;

import com.playtomic.tests.wallet.model.api.WalletResponse;

import java.util.concurrent.CompletableFuture;

public interface WalletBusinessLayer {
    CompletableFuture<WalletResponse> getWalletById(Long walletId);
}