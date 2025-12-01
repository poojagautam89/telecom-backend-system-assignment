package com.telecom.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGSERIAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sim_id", referencedColumnName = "sim_id",
            foreignKey = @ForeignKey(name = "fk_bill_sim"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Sim sim;

    @Column(name = "billing_month", nullable = false, length = 7)
    private String billingMonth; // "YYYY-MM"

    @Column(name = "base_amount", precision = 12, scale = 2)
    private BigDecimal baseAmount = BigDecimal.ZERO;

    @Column(name = "overage_amount", precision = 12, scale = 2)
    private BigDecimal overageAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

}
