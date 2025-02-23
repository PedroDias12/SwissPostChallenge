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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class WalletAssetControllerTest {
    @Autowired
    private MockMvc mockMvc;
/*
    @Order(1)
    @Test
    void testAddAssetToWallet() throws Exception {
                mockMvc.perform(get("/wallets/1?details=true"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value("1"))
                        .andExpect(jsonPath("$.total").isNumber())
                        .andExpect(jsonPath("$.assets[0]").exists())
                        .andExpect(jsonPath("$.assets[0].symbol").value("ETH"))
                        .andExpect(jsonPath("$.assets[0].quantity").value(5.5))
                        .andExpect(jsonPath("$.assets[0].price").isNumber())
                        .andExpect(jsonPath("$.assets[0].value").isNumber());

        String requestBody = "{\"symbol\": \"ETH\", \"quantity\": 5.5, \"price\": 3000}";
        mockMvc.perform(put("/wallets/{wallet_id}/assets", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    void testGetWallet() throws Exception {
        mockMvc.perform(get("/wallets/1?details=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.total").isNumber())
                .andExpect(jsonPath("$.assets[0]").exists())
                .andExpect(jsonPath("$.assets[0].symbol").value("ETH"))
                .andExpect(jsonPath("$.assets[0].quantity").value(5.5))
                .andExpect(jsonPath("$.assets[0].price").isNumber())
                .andExpect(jsonPath("$.assets[0].value").isNumber());
    }*/
}
