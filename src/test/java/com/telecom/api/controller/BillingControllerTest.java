package com.telecom.api.controller;

import com.telecom.api.dto.InvoiceResponseDTO;
import com.telecom.api.service.BillingService;
import com.telecom.api.service.InvoiceService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class BillingControllerInvoiceTest {

    @Mock private InvoiceService invoiceService;
    @Mock private BillingService billingService;
    @InjectMocks private BillingController billingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(billingController).build();
    }

    @Test
    void getInvoiceJson_returns200() throws Exception {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setBillId(200L);
        dto.setTotalAmount(BigDecimal.valueOf(125));
        dto.setBillingMonth("2025-11");
        dto.setGeneratedAt(LocalDateTime.now());
        when(invoiceService.getInvoice(200L)).thenReturn(dto);

        mockMvc.perform(get("/api/billing/200/invoice")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billId").value(200));
    }

    @Test
    void getInvoicePdf_returnsPdf() throws Exception {
        when(invoiceService.generateInvoicePdf(200L)).thenReturn(new byte[]{1,2,3});
        mockMvc.perform(get("/api/billing/200/invoice/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=invoice-200.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
