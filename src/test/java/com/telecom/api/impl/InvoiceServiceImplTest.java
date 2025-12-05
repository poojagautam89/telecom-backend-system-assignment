package com.telecom.api.impl;

import com.telecom.api.entity.Bill;
import com.telecom.api.entity.Sim;
import com.telecom.api.repository.BillRepository;
import com.telecom.api.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private BillRepository billRepo;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Bill bill;

    @BeforeEach
    void setUp() {
        bill = new Bill();
        bill.setId(200L);
        bill.setBillingMonth("2025-11");
        bill.setBaseAmount(BigDecimal.valueOf(100));
        bill.setOverageAmount(BigDecimal.valueOf(25));
        bill.setTotalAmount(BigDecimal.valueOf(125));
        bill.setGeneratedAt(LocalDateTime.now());
        Sim s = new Sim();
        s.setMsisdn("999999");
        bill.setSim(s);
    }

    @Test
    void getInvoice_returnsDto() {
        when(billRepo.findById(200L)).thenReturn(Optional.of(bill));
        var dto = invoiceService.getInvoice(200L);
        assertThat(dto).isNotNull();
        assertThat(dto.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(125));
    }

    @Test
    void generatePdf_returnsBytes() {
        when(billRepo.findById(200L)).thenReturn(Optional.of(bill));
        byte[] pdf = invoiceService.generateInvoicePdf(200L);
        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(0);
    }
}
