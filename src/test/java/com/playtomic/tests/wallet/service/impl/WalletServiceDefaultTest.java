package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletServiceException;
import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.repository.WalletEntity;
import com.playtomic.tests.wallet.model.service.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceDefaultTest {

    @Mock
    WalletMapper walletMapperMock;
    @Mock
    WalletRepository walletRepositoryMock;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWalletById_OK() {
        //Given
        Long id = 1L;
        WalletEntity repositoryWallet = WalletEntity.builder().id(id).balance(BigDecimal.TEN).build();
        Wallet serviceWallet = Wallet.builder().id(id).balance(BigDecimal.TEN).build();
        Wallet expected = Wallet.builder().id(id).balance(BigDecimal.TEN).build();

        when(walletMapperMock.repositoryToService(repositoryWallet)).thenReturn(serviceWallet);
        when(walletRepositoryMock.findOne(id)).thenReturn(repositoryWallet);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        Optional<Wallet> result = service.getWalletById(id);

        //Then
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void getWalletById_EmptyOnRepoNull() {
        //Given
        Long id = 1L;

        when(walletRepositoryMock.findOne(id)).thenReturn(null);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        Optional<Wallet> result = service.getWalletById(id);

        //Then
        assertFalse(result.isPresent());
        verify(walletMapperMock, never()).repositoryToService(any());
    }

    @Test
    public void chargeWalletById_OK() throws WalletServiceException {
        //Given
        Long id = 1L;
        BigDecimal amount = BigDecimal.ONE;
        WalletEntity repositoryWallet = WalletEntity.builder().id(id).balance(BigDecimal.TEN).build();
        Wallet serviceWallet = Wallet.builder().id(id).balance(new BigDecimal("9")).build();
        Wallet expected = Wallet.builder().id(id).balance(new BigDecimal("9")).build();
        WalletEntity chargedWallet = repositoryWallet.toBuilder().balance(repositoryWallet.getBalance().subtract(amount)).build();

        when(walletRepositoryMock.findOne(id)).thenReturn(repositoryWallet);
        when(walletRepositoryMock.save(chargedWallet)).thenReturn(chargedWallet);
        when(walletMapperMock.repositoryToService(chargedWallet)).thenReturn(serviceWallet);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        Optional<Wallet> result = service.chargeWalletById(id, amount);

        //Then
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void chargeWalletById_Balance0_OK() throws WalletServiceException {
        //Given
        Long id = 1L;
        BigDecimal amount = BigDecimal.TEN;
        WalletEntity repositoryWallet = WalletEntity.builder().id(id).balance(BigDecimal.TEN).build();
        Wallet serviceWallet = Wallet.builder().id(id).balance(BigDecimal.ZERO).build();
        Wallet expected = Wallet.builder().id(id).balance(new BigDecimal("0")).build();
        WalletEntity chargedWallet = repositoryWallet.toBuilder().balance(repositoryWallet.getBalance().subtract(amount)).build();

        when(walletRepositoryMock.findOne(id)).thenReturn(repositoryWallet);
        when(walletRepositoryMock.save(chargedWallet)).thenReturn(chargedWallet);
        when(walletMapperMock.repositoryToService(chargedWallet)).thenReturn(serviceWallet);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        Optional<Wallet> result = service.chargeWalletById(id, amount);

        //Then
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void chargeWalletById_AmountGreaterThanBalance() throws WalletServiceException {
        //Given
        exceptionRule.expect(isA(WalletServiceException.class));
        exceptionRule.expectMessage("Amount to charge: 10 is greater than current wallet balance: 1");

        Long id = 1L;
        BigDecimal amount = BigDecimal.TEN;
        WalletEntity repositoryWallet = WalletEntity.builder().id(id).balance(BigDecimal.ONE).build();
        Wallet expected = Wallet.builder().id(id).balance(new BigDecimal("0")).build();

        when(walletRepositoryMock.findOne(id)).thenReturn(repositoryWallet);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        service.chargeWalletById(id, amount);

        //Then
        verify(walletRepositoryMock, never()).save(any(WalletEntity.class));

    }

    @Test
    public void rechargeWalletById_OK() {
        //Given
        Long id = 1L;
        BigDecimal amount = BigDecimal.ONE;
        WalletEntity repositoryWallet = WalletEntity.builder().id(id).balance(BigDecimal.TEN).build();
        Wallet serviceWallet = Wallet.builder().id(id).balance(new BigDecimal("11")).build();
        Wallet expected = Wallet.builder().id(id).balance(new BigDecimal("11")).build();
        WalletEntity rechargedWallet = repositoryWallet.toBuilder().balance(repositoryWallet.getBalance().add(amount)).build();

        when(walletRepositoryMock.findOne(id)).thenReturn(repositoryWallet);
        when(walletRepositoryMock.save(rechargedWallet)).thenReturn(rechargedWallet);
        when(walletMapperMock.repositoryToService(rechargedWallet)).thenReturn(serviceWallet);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        Optional<Wallet> result = service.rechargeWalletById(id, amount);

        //Then
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void rechargeWalletById_nullOnRepository() {
        //Given
        Long id = 1L;
        BigDecimal amount = BigDecimal.ONE;

        when(walletRepositoryMock.findOne(id)).thenReturn(null);

        WalletServiceDefault service = new WalletServiceDefault(walletRepositoryMock, walletMapperMock);

        //When
        Optional<Wallet> result = service.rechargeWalletById(id, amount);

        //Then
        assertFalse(result.isPresent());
        verify(walletRepositoryMock, never()).save(any(WalletEntity.class));
        verify(walletMapperMock, never()).repositoryToService(any());
    }

}
