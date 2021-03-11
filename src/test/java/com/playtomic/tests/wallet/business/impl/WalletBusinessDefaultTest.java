package com.playtomic.tests.wallet.business.impl;

import com.playtomic.tests.wallet.exception.ParseAmountException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.mapper.BigDecimalMapper;
import com.playtomic.tests.wallet.mapper.WalletMapper;
import com.playtomic.tests.wallet.model.api.WalletResponse;
import com.playtomic.tests.wallet.model.service.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletBusinessDefaultTest {

    @Mock
    WalletService walletServiceMock;
    @Mock
    WalletMapper walletMapperMock;
    @Mock
    BigDecimalMapper bdMapperMock;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWalletById_OK() throws ExecutionException, InterruptedException {
        //Given
        Long id = 1L;
        Wallet serviceWallet = Wallet.builder().id(id).balance(BigDecimal.TEN).build();
        WalletResponse responseWallet = WalletResponse.builder().id(id).balance("10.0").build();
        WalletResponse expected = WalletResponse.builder().id(id).balance("10.0").build();

        when(walletServiceMock.getWalletById(id)).thenReturn(Optional.of(serviceWallet));
        when(walletMapperMock.serviceToRestResponse(serviceWallet)).thenReturn(responseWallet);

        WalletBusinessLayerDefault service = new WalletBusinessLayerDefault(walletServiceMock, walletMapperMock, bdMapperMock);

        //When
        CompletableFuture<WalletResponse> result = service.getWalletById(id);

        //Then
        assertEquals(expected, result.get());
    }

    @Test
    public void getWalletById_serviceReturnsEmpty_KO() {
        //Given
        Long id = 1L;

        exceptionRule.expect(isA(WalletNotFoundException.class));
        exceptionRule.expectMessage("Unable to find wallet with id: " + id);

        when(walletServiceMock.getWalletById(1L)).thenReturn(Optional.empty());

        WalletBusinessLayerDefault service = new WalletBusinessLayerDefault(walletServiceMock, walletMapperMock, bdMapperMock);

        //When
        CompletableFuture<WalletResponse> result = service.getWalletById(id);

        //Then
        assertTrue(result.isCompletedExceptionally());
    }

    @Test
    public void chargeWalletById_OK() throws ExecutionException, InterruptedException {
        //Given
        Long id = 1L;
        String amount = "15.6567";
        BigDecimal bdAmount = new BigDecimal(amount);
        Wallet serviceWallet = Wallet.builder().id(id).balance(BigDecimal.TEN).build();
        WalletResponse responseWallet = WalletResponse.builder().id(id).balance("10.0").build();
        WalletResponse expected = WalletResponse.builder().id(id).balance("10.0").build();

        when(bdMapperMock.stringToBigDecimal(amount)).thenReturn(Optional.of(bdAmount));
        when(walletServiceMock.chargeWalletById(id, bdAmount)).thenReturn(Optional.of(serviceWallet));
        when(walletMapperMock.serviceToRestResponse(serviceWallet)).thenReturn(responseWallet);

        WalletBusinessLayerDefault service = new WalletBusinessLayerDefault(walletServiceMock, walletMapperMock, bdMapperMock);

        //When
        CompletableFuture<WalletResponse> result = service.chargeWalletById(id, amount);

        //Then
        assertEquals(expected, result.get());
    }

    @Test
    public void chargeWalletById_ErrorFormattingAmount() {
        //Given
        Long id = 1L;
        String amount = "error";

        exceptionRule.expect(isA(ParseAmountException.class));
        exceptionRule.expectMessage("Unable to parse amount: " + amount);

        when(bdMapperMock.stringToBigDecimal(amount)).thenReturn(Optional.empty());

        WalletBusinessLayerDefault service = new WalletBusinessLayerDefault(walletServiceMock, walletMapperMock, bdMapperMock);

        //When
        CompletableFuture<WalletResponse> result = service.chargeWalletById(id, amount);

        //Then
        assertTrue(result.isCompletedExceptionally());
    }

}
