package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.service.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceDefault implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletServiceDefault(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public Optional<Wallet> getWalletById(Long id) {
        return Optional.ofNullable(walletRepository.findOne(id))
                .map(walletMapper::repositoryToService);
    }

    @Override
    public Optional<Wallet> chargeWalletById(Long id, BigDecimal amount) {
        return Optional.empty();
    }
}
