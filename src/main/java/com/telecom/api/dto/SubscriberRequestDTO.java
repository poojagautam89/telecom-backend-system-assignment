package com.telecom.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;

@Data
public class SubscriberRequestDTO {
    @NotBlank(message = "fullName is required")
    private String fullName;

    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "phoneNumber is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "phoneNumber must be numeric (10-15 digits)")
    private String phoneNumber;

    private String address;
}
