package com.telecom.api.dto;

import com.telecom.api.enum_pack.SimStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class SimResponseDTO {

    private UUID simId;
    private String imsi;
    private String msisdn;
    private UUID subscriberId;
    private UUID planId;
    private SimStatus status;

}
