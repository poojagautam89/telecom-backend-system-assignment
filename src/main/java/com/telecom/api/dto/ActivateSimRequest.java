package com.telecom.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class ActivateSimRequest {
    @NotNull
    private UUID subscriberId;
    @NotNull
    private String imsi;
    private String msisdn;

}
