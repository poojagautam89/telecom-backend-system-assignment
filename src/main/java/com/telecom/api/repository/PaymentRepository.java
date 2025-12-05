package com.telecom.api.repository;

import com.telecom.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByBillId(Long billId);
    List<Payment> findBySubscriberSubscriberId(UUID subscriberId);
    boolean existsByTransactionId(String transactionId);
}
