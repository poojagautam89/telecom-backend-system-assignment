package com.telecom.api.entity;

import com.telecom.api.enum_pack.SimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sim {

    @Id
    @Column(name = "sim_id", updatable = false, nullable = false)
    private UUID simId;

    @Column(name = "imsi", unique = true, nullable = false, length = 64)
    private String imsi;

    @Column(name = "msisdn", unique = true, length = 32)
    private String msisdn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", referencedColumnName = "subscriber_id",
            foreignKey = @ForeignKey(name = "fk_sims_subscriber"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Subscriber subscriber;

    // Plan relation as ManyToOne (Plan uses UUID planId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", referencedColumnName = "plan_id",
            foreignKey = @ForeignKey(name = "fk_sims_plan"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private SimStatus status = SimStatus.INACTIVE;

    @Column(name = "activation_date")
    private LocalDate activationDate;


    @PrePersist
    public void prePersist() {
        if (simId == null) {
            simId = UUID.randomUUID();
        }
        if (status == null) {
            status = SimStatus.INACTIVE;
        }
    }
}
