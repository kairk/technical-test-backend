package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.model.repository.Wallet;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.walletRepository.deleteAll();
    }

    @Test
    public void getWalletById_Ok() throws Exception {
        walletRepository.save(Wallet.builder().id(1L).balance(BigDecimal.TEN).build());

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
                .perform(get("/v1/wallet/{id}", 1L)).andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isNoContent());
    }
}
