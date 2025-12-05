package com.telecom.api.service;

import com.telecom.api.dto.PaymentRequestDTO;
import com.telecom.api.dto.PaymentResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponseDTO makePayment(PaymentRequestDTO request);
    List<PaymentResponseDTO> getPaymentsByBill(Long billId);
    List<PaymentResponseDTO> getPaymentsBySubscriber(UUID subscriberId);
}
