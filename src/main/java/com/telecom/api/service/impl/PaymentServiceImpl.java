package com.telecom.api.service.impl;

import com.telecom.api.dto.PaymentRequestDTO;
import com.telecom.api.dto.PaymentResponseDTO;
import com.telecom.api.entity.Bill;
import com.telecom.api.entity.Payment;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.enum_pack.PaymentStatus;
import com.telecom.api.repository.BillRepository;
import com.telecom.api.repository.PaymentRepository;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final BillRepository billRepo;
    private final SubscriberRepository subscriberRepo;

    public PaymentServiceImpl(PaymentRepository paymentRepo,
                              BillRepository billRepo,
                              SubscriberRepository subscriberRepo) {
        this.paymentRepo = paymentRepo;
        this.billRepo = billRepo;
        this.subscriberRepo = subscriberRepo;
    }

    @Override
    @Transactional
    public PaymentResponseDTO makePayment(PaymentRequestDTO req) {
        // validate bill
        Bill bill = billRepo.findById(req.getBillId())
                .orElseThrow(() -> new IllegalArgumentException("Bill not found: " + req.getBillId()));

        // validate subscriber
        Subscriber sub = subscriberRepo.findById(req.getSubscriberId())
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found: " + req.getSubscriberId()));

        // duplicate tx check
        if (req.getTransactionId() != null && paymentRepo.existsByTransactionId(req.getTransactionId())) {
            throw new IllegalArgumentException("Duplicate transaction id");
        }

        Payment p = Payment.builder()
                .bill(bill)
                .subscriber(sub)
                .amount(req.getAmount())
                .mode(req.getMode())
                .transactionId(req.getTransactionId())
                .status(PaymentStatus.SUCCESS) // for MVP mark SUCCESS; gateway integration later
                .paidAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepo.save(p);

        // OPTIONAL: update Bill paid_amount / status if you add fields to Bill
        return toDto(saved);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByBill(Long billId) {
        return paymentRepo.findByBillId(billId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsBySubscriber(UUID subscriberId) {
        return paymentRepo.findBySubscriberSubscriberId(subscriberId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private PaymentResponseDTO toDto(Payment p) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(p.getPaymentId());
        dto.setBillId(p.getBill() != null ? p.getBill().getId() : null);
        dto.setSubscriberId(p.getSubscriber() != null ? p.getSubscriber().getSubscriberId() : null);
        dto.setAmount(p.getAmount());
        dto.setMode(p.getMode());
        dto.setTransactionId(p.getTransactionId());
        dto.setStatus(p.getStatus());
        dto.setPaidAt(p.getPaidAt());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
