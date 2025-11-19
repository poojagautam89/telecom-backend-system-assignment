package com.telecom.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscribers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriber {

    @Id
    @Column(name = "subscriber_id", updatable = false, nullable = false)
    private UUID subscriberId;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    @Column(name = "activation_date")
    private LocalDate activationDate;

    private String status; // e.g. ACTIVE / SUSPENDED / INACTIVE

    @PrePersist
    public void prePersist() {
        if (subscriberId == null) {
            subscriberId = UUID.randomUUID();
        }
        if (status == null) {
            status = "INACTIVE";
        }
    }

    //  method business helpers
    public void activate() {
        this.status = "ACTIVE";
        this.activationDate = LocalDate.now();
    }

    public void suspend() {
        this.status = "SUSPENDED";
    }
}
