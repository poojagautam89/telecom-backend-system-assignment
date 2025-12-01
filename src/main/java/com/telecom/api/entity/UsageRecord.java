package com.telecom.api.entity;

import com.telecom.api.enum_pack.UsageType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usage_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGSERIAL

    // reference to Sim (UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sim_id", referencedColumnName = "sim_id",
            foreignKey = @ForeignKey(name = "fk_usage_record_sim"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Sim sim;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type", nullable = false, length = 16)
    private UsageType usageType; // CALL, DATA, SMS

    @Column(name = "usage_value", nullable = false)
    private Long usageValue; // KB for DATA, minutes for CALL, count for SMS

    @Column(name = "usage_timestamp", nullable = false)
    private LocalDateTime usageTimestamp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
