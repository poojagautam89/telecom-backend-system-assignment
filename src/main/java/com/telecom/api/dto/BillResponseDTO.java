package com.telecom.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BillResponseDTO {
    private Long id;
    private UUID simId;
    private String billingMonth;
    private BigDecimal baseAmount;
    private BigDecimal overageAmount;
    private BigDecimal totalAmount;
    private LocalDateTime generatedAt;
    // getters & setters
}
