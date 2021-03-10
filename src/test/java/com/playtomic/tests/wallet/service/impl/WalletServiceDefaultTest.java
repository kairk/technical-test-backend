package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.service.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceDefaultTest {

    @Mock
    WalletMapper walletMapperMock;
    @Mock
    WalletRepository walletRepositoryMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWalletById_OK() {
        //Given
        Long id = 1L;
        com.playtomic.tests.wallet.model.repository.Wallet repositoryWallet = com.playtomic.tests.wallet.model.repository.Wallet.builder().id(id).balance(BigDecimal.TEN).build();
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
}
