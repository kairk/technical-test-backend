package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.model.api.WalletResponse;
import com.playtomic.tests.wallet.model.repository.WalletEntity;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class WalletMapperTest {
    private WalletMapper mapper = Mappers.getMapper(WalletMapper.class);

    @Test
    public void respositoryToService_OK() {
        //Given
        WalletEntity repositoryWallet = WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build();
        com.playtomic.tests.wallet.model.service.Wallet expected =
                com.playtomic.tests.wallet.model.service.Wallet.builder().id(1L).balance(BigDecimal.TEN).build();

        //When
        com.playtomic.tests.wallet.model.service.Wallet result = mapper.repositoryToService(repositoryWallet);

        //Then
        assertEquals(expected, result);
    }

    @Test
    public void serviceToRestResponse_OK() {
        //Given
        com.playtomic.tests.wallet.model.service.Wallet serviceWallet =
                com.playtomic.tests.wallet.model.service.Wallet.builder().id(1L).balance(new BigDecimal("10.58746")).build();
        WalletResponse expected = WalletResponse.builder().id(1L).balance("10.5875").build();

        //When
        WalletResponse result = mapper.serviceToRestResponse(serviceWallet);

        //Then
        assertEquals(expected, result);
    }

}
