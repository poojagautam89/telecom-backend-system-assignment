package com.telecom.api.dto;

import com.telecom.api.enum_pack.PlanType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class PlanRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private PlanType type;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal monthlyPrice;

    @NotNull
    private Long dataAllowanceMb;

    @NotNull
    private Long callAllowanceMin;

    @NotNull
    private Long smsAllowance;

    // getters/setters
}
