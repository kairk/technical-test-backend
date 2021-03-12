package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.WalletServiceException;
import com.playtomic.tests.wallet.model.service.Wallet;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletService {
    Optional<Wallet> getWalletById(Long id);

    Optional<Wallet> chargeWalletById(Long id, BigDecimal amount) throws WalletServiceException;

    Optional<Wallet> rechargeWalletById(Long id, BigDecimal amount) throws WalletServiceException;
}
