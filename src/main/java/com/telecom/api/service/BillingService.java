package com.telecom.api.service;

import com.telecom.api.entity.Bill;
import java.time.YearMonth;
import java.util.UUID;

public interface BillingService {
    Bill generateBill(UUID simId, YearMonth month);
}
