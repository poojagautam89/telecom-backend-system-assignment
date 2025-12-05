package com.telecom.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class InvoiceResponseDTO {
    private Long billId;
    private String billingMonth;
    private String subscriberName;
    private String subscriberEmail;
    private String msisdn;
    private BigDecimal baseAmount;
    private BigDecimal overageAmount;
    private BigDecimal totalAmount;
    private List<InvoiceLineItemDTO> items;
    private LocalDateTime generatedAt;


}
