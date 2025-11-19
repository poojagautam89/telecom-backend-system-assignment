package com.telecom.api.dto;

import lombok.Data;

@Data
public class SubscriberRequestDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
