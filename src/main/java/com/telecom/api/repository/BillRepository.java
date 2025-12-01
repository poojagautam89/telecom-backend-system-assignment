package com.telecom.api.repository;

import com.telecom.api.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findBySimSimIdAndBillingMonth(UUID simId, String billingMonth);
}
