package com.telecom.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UsageRequest {
    @NotNull
    private UUID simId;

    @NotNull
    private String type; // CALL, DATA, SMS

    @NotNull
    private Long value; // KB for DATA, minutes for CALL, count for SMS

    @NotNull
    private LocalDateTime timestamp;

    // getters & setters
}
