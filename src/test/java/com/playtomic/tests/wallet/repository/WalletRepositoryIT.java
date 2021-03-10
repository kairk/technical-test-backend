package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.repository.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletRepositoryIT {

    @Resource
    private WalletRepository walletRepository;

    @Test
    public void findOne_OK() {
        Long id = 1L;
        Wallet wallet = Wallet.builder().id(id).balance(BigDecimal.TEN).build();
        walletRepository.save(wallet);

        Wallet findWallet = walletRepository.findOne(id);

        assertEquals(wallet.getId(), findWallet.getId());
        assertEquals(0, wallet.getBalance().compareTo(findWallet.getBalance()));
    }

    @Test
    public void findOne_nullWhenNoExists() {
        Long id = 1L;
        Wallet wallet = Wallet.builder().id(id).balance(BigDecimal.TEN).build();
        walletRepository.save(wallet);

        Wallet findWallet = walletRepository.findOne(5L);

        assertNull(findWallet);
    }
}
