package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.service.Wallet;

import java.util.Optional;

public interface WalletService {
    Optional<Wallet> getWalletById(Long id);
}
