package com.telecom.api.entity;

import com.telecom.api.enum_pack.PlanType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @Column(name = "plan_id", updatable = false, nullable = false)
    private UUID planId;

    @Column(name = "plan_name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 20)
    private PlanType type;  // PREPAID / POSTPAID

    @Column(name = "monthly_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal monthlyPrice;

    // Allowances (as per client billing rules − Sprint-2)
    @Column(name = "data_allowance_mb", nullable = false)
    private Long dataAllowanceMb;     // Data allowance MB

    @Column(name = "call_allowance_min", nullable = false)
    private Long callAllowanceMin;    // Call minutes allowance

    @Column(name = "sms_allowance", nullable = false)
    private Long smsAllowance;        // SMS allowance

    @PrePersist
    public void prePersist() {
        if (planId == null) {
            planId = UUID.randomUUID();
        }
        if (monthlyPrice == null) {
            monthlyPrice = BigDecimal.ZERO;
        }
        if (dataAllowanceMb == null) {
            dataAllowanceMb = 0L;
        }
        if (callAllowanceMin == null) {
            callAllowanceMin = 0L;
        }
        if (smsAllowance == null) {
            smsAllowance = 0L;
        }
    }
}
