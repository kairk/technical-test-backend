package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.model.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WalletRepository walletRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getWalletById_Ok() throws Exception {
        walletRepository.save(WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build());

        MvcResult result = mockMvc
                .perform(get("/v1/wallet/{id}", 1L)).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("balance").value("10"));
    }

    @Test
    public void getWalletById_NoContent() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/v1/wallet/{id}", 5L)).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isNoContent());
    }

    @Test
    public void chargeWallet_Ok() throws Exception {
        walletRepository.save(WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build());

        MvcResult result = mockMvc
                .perform(put("/v1/wallet/{id}/charge", 1L).content("9")).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("balance").value("1"));
    }

    @Test
    public void chargeWallet_balanceZero_Ok() throws Exception {
        walletRepository.save(WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build());

        MvcResult result = mockMvc
                .perform(put("/v1/wallet/{id}/charge", 1L).content("10")).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("balance").value("0"));
    }

    @Test
    public void chargeWallet_unableToParseAmount() throws Exception {
        walletRepository.save(WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build());

        MvcResult result = mockMvc
                .perform(put("/v1/wallet/{id}/charge", 1L).content("error")).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void chargeWallet_unableToFindWallet() throws Exception {
        walletRepository.save(WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build());

        MvcResult result = mockMvc
                .perform(put("/v1/wallet/{id}/charge", 5L).content("8")).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isNoContent());
    }

    @Test
    public void chargeWallet_amountGreaterThanBalance() throws Exception {
        walletRepository.save(WalletEntity.builder().id(1L).balance(BigDecimal.TEN).build());

        MvcResult result = mockMvc
                .perform(put("/v1/wallet/{id}/charge", 1L).content("11")).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isInternalServerError());
    }

    //TODO:RECHARGE ENDPOINTS

}
