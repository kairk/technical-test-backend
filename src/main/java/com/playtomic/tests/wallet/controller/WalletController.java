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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(Consts.API_VERSION + Consts.WALLET_ENDPOIT)
@Api(tags = "wallet")
//TODO: Validation layer with @Validated
public class WalletController {

    private final WalletBusinessLayer walletBusiness;

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

    @PutMapping(path = "/{id}/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Substract an ammount from a wallet")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Wallet updated"),
            @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Wallet not found"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Internal error processing request")
    }
    )
    public CompletableFuture<ResponseEntity<WalletResponse>> chargeWallet(
            @PathVariable Long id,
            @RequestBody String amount) {
        return walletBusiness.chargeWalletById(id, amount).thenApply(ResponseEntity::ok);
    }
}
