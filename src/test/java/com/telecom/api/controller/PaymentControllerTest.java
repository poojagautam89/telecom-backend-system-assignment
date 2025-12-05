package com.telecom.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.api.dto.PaymentRequestDTO;
import com.telecom.api.dto.PaymentResponseDTO;
import com.telecom.api.enum_pack.PaymentMode;
import com.telecom.api.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void makePayment_returns201() throws Exception {
        PaymentRequestDTO req = new PaymentRequestDTO();
        req.setBillId(100L);
        req.setSubscriberId(UUID.randomUUID());
        req.setAmount(BigDecimal.valueOf(123.45));
        req.setMode(PaymentMode.UPI);
        req.setTransactionId("tx-abc");

        PaymentResponseDTO resp = new PaymentResponseDTO();
        resp.setPaymentId(UUID.randomUUID());
        resp.setBillId(100L);
        resp.setSubscriberId(req.getSubscriberId());
        resp.setAmount(req.getAmount());
        resp.setMode(req.getMode());
        resp.setTransactionId(req.getTransactionId());
        resp.setStatus(com.telecom.api.enum_pack.PaymentStatus.SUCCESS);
        resp.setPaidAt(LocalDateTime.now());

        when(paymentService.makePayment(any())).thenReturn(resp);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(123.45))
                .andExpect(jsonPath("$.transactionId").value("tx-abc"));
    }

    @Test
    void getByBill_returns200() throws Exception {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(UUID.randomUUID());
        dto.setBillId(100L);
        dto.setAmount(BigDecimal.valueOf(50));
        when(paymentService.getPaymentsByBill(100L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/payments/bill/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].billId").value(100));
    }
}
