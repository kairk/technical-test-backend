package com.playtomic.tests.wallet.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Consts.API_VERSION + Consts.WALLET_ENDPOIT)
@Api(tags = "wallet")
//@Validated
public class WalletController {

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }
}
