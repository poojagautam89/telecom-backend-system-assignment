package com.telecom.api.dto;

import com.telecom.api.enum_pack.PlanType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
public class PlanResponseDTO {
    private UUID planId;
    private String name;
    private PlanType type;
    private BigDecimal monthlyPrice;
    private Long dataAllowanceMb;
    private Long callAllowanceMin;
    private Long smsAllowance;
    // getters/setters
}
