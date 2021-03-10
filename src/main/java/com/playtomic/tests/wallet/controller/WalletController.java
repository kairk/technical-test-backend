package com.playtomic.tests.wallet.controller;

import com.playtomic.tests.wallet.business.WalletBusinessLayer;
import com.playtomic.tests.wallet.model.api.WalletResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(Consts.API_VERSION + Consts.WALLET_ENDPOIT)
@Api(tags = "wallet")
//@Validated
public class WalletController {

    private WalletBusinessLayer walletBusiness;

    @Autowired
    public WalletController(WalletBusinessLayer walletBusiness) {
        this.walletBusiness = walletBusiness;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find a Wallet by ID")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Wallet found"),
            @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Wallet not found"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Internal error processing request")
    }
    )
    public CompletableFuture<ResponseEntity<WalletResponse>> getWalletById(
            @PathVariable Long id) {
        return walletBusiness.getWalletById(id).thenApply(ResponseEntity::ok);
    }
}
