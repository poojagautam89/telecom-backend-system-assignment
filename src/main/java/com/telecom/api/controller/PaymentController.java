package com.telecom.api.controller;

import com.telecom.api.dto.PaymentRequestDTO;
import com.telecom.api.dto.PaymentResponseDTO;
import com.telecom.api.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ============================
    // USER + ADMIN can make payment
    // ============================
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> makePayment(@Valid @RequestBody PaymentRequestDTO req) {
        PaymentResponseDTO resp = paymentService.makePayment(req);
        return ResponseEntity.status(201).body(resp);
    }

    // ============================
    // ADMIN ONLY: View payments by bill
    // ============================
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<PaymentResponseDTO>> byBill(@PathVariable("billId") Long billId) {
        return ResponseEntity.ok(paymentService.getPaymentsByBill(billId));
    }

    // ============================
    // ADMIN ONLY: View payments by subscriber
    // ============================
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/subscriber/{subscriberId}")
    public ResponseEntity<List<PaymentResponseDTO>> bySubscriber(@PathVariable("subscriberId") UUID subscriberId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySubscriber(subscriberId));
    }
}
