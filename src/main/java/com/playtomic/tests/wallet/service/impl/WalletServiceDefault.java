package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletServiceException;
import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.repository.WalletEntity;
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
    public Optional<Wallet> chargeWalletById(Long id, BigDecimal amount) throws WalletServiceException {
        Optional<WalletEntity> findWallet = Optional.ofNullable(walletRepository.findOne(id));
        Optional<Wallet> result = Optional.empty();

        if (findWallet.isPresent()) {
            WalletEntity walletEntity = findWallet.get();
            if (canChargeAmount(amount, walletEntity)) {
                WalletEntity chargedWallet = chargeAmount(amount, walletEntity);
                WalletEntity walletResult = walletRepository.save(chargedWallet);
                result = Optional.of(walletMapper.repositoryToService(walletResult));
            } else {
                throw new WalletServiceException("Amount to charge: " + amount + " is greater than current wallet balance: " + walletEntity.getBalance());
            }
        }


        return result;
    }

    @Override
    public Optional<Wallet> rechargeWalletById(Long id, BigDecimal amount) throws WalletServiceException {
        return Optional.empty();
    }

    private WalletEntity chargeAmount(BigDecimal amount, WalletEntity walletEntity) {
        return walletEntity.toBuilder().balance(walletEntity.getBalance().subtract(amount)).build();
    }

    private boolean canChargeAmount(BigDecimal amount, WalletEntity walletEntity) {
        return walletEntity.getBalance().compareTo(amount) >= 0;
    }
}
