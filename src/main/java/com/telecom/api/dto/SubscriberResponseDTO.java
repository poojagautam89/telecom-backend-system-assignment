package com.telecom.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriberResponseDTO {
    private UUID subscriberId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate activationDate;
    private String status;
}
