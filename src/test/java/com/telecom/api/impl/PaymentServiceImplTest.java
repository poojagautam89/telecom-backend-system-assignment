package com.telecom.api.impl;

import com.telecom.api.dto.PaymentRequestDTO;
import com.telecom.api.entity.Bill;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.enum_pack.PaymentMode;
import com.telecom.api.repository.BillRepository;
import com.telecom.api.repository.PaymentRepository;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private BillRepository billRepo;

    @Mock
    private SubscriberRepository subscriberRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Bill bill;
    private Subscriber subscriber;

    @BeforeEach
    void setUp() {
        bill = new Bill();
        bill.setId(100L);

        subscriber = new Subscriber();
        subscriber.setSubscriberId(UUID.randomUUID());
    }

    @Test
    void makePayment_success() {
        PaymentRequestDTO req = new PaymentRequestDTO();
        req.setBillId(100L);
        req.setSubscriberId(subscriber.getSubscriberId());
        req.setAmount(BigDecimal.valueOf(250.50));
        req.setMode(PaymentMode.UPI);
        req.setTransactionId("tx-123");

        when(billRepo.findById(100L)).thenReturn(Optional.of(bill));
        when(subscriberRepo.findById(subscriber.getSubscriberId())).thenReturn(Optional.of(subscriber));
        when(paymentRepo.existsByTransactionId("tx-123")).thenReturn(false);
        when(paymentRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var resp = paymentService.makePayment(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(250.50));
        verify(paymentRepo, times(1)).save(any());
    }

    @Test
    void makePayment_duplicateTx_throws() {
        PaymentRequestDTO req = new PaymentRequestDTO();
        req.setBillId(100L);
        req.setSubscriberId(subscriber.getSubscriberId());
        req.setAmount(BigDecimal.valueOf(50));
        req.setMode(PaymentMode.CARD);
        req.setTransactionId("tx-dup");

        when(billRepo.findById(100L)).thenReturn(Optional.of(bill));
        when(subscriberRepo.findById(subscriber.getSubscriberId())).thenReturn(Optional.of(subscriber));
        when(paymentRepo.existsByTransactionId("tx-dup")).thenReturn(true);

        try {
            paymentService.makePayment(req);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Duplicate transaction id");
        }

        verify(paymentRepo, never()).save(any());
    }
}
