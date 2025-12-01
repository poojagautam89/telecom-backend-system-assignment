package com.telecom.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.api.entity.Bill;
import com.telecom.api.service.BillingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillingController.class)
@ExtendWith(MockitoExtension.class)
class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BillingService billingService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void generateBill_returns200() throws Exception {
        UUID simId = UUID.randomUUID();
        String month = "2025-11";

        Bill bill = new Bill();
        bill.setId(1L);
        bill.setBillingMonth(month);
        bill.setTotalAmount(BigDecimal.valueOf(250.50));
        bill.setGeneratedAt(LocalDateTime.now());

        Mockito.when(billingService.generateBill(eq(simId), eq(YearMonth.parse(month))))
                .thenReturn(bill);

        mockMvc.perform(get("/api/billing/generate/" + simId + "/" + month)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billingMonth").value(month))
                .andExpect(jsonPath("$.totalAmount").value(250.50));
    }
}
