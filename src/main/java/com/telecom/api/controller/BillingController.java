package com.telecom.api.controller;


import com.telecom.api.entity.Bill;
import com.telecom.api.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.UUID;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/generate/{simId}/{month}")
    public ResponseEntity<Bill> generate(@PathVariable UUID simId, @PathVariable String month) {
        YearMonth ym = YearMonth.parse(month); // expects "YYYY-MM"
        Bill bill = billingService.generateBill(simId, ym);
        return ResponseEntity.ok(bill);
    }
}

