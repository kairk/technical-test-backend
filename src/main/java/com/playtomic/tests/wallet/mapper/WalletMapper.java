package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.model.api.WalletResponse;
import com.playtomic.tests.wallet.model.repository.WalletEntity;
import com.playtomic.tests.wallet.model.service.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WalletMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "balance", target = "balance")
    Wallet repositoryToService(WalletEntity data);

    @Mapping(source = "balance", target = "balance", numberFormat = "#.####")
    WalletResponse serviceToRestResponse(Wallet wallet);
}
