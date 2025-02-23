package com.example.challenge.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @Test
    void testCreateWallet() throws Exception {
        mockMvc.perform(get("/wallets/1"))
                .andExpect(status().isNotFound());

        String email = "test@example.com";
        mockMvc.perform(post("/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Order(2)
    @Test
    void testGetWallet() throws Exception {
        mockMvc.perform(get("/wallets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Order(3)
    @Test
    void testWalletPerformance() throws Exception {
        String walletToEvaluate = "{\"assets\": [{\"symbol\": \"BTC\",\"quantity\": 0.5,\"value\": 35000},{\"symbol\": \"ETH\",\"quantity\": 42.42,\"value\": 118385.31}]}";
        mockMvc.perform(post("/wallets/performance?date=2025-02-22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletToEvaluate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("163906.41"))
                .andExpect(jsonPath("$.bestAsset").value("BTC"))
                .andExpect(jsonPath("$.bestPerformance").value("37.85"))
                .andExpect(jsonPath("$.worstAsset").value("ETH"))
                .andExpect(jsonPath("$.worstPerformance").value("-2.31"));
    }
}
