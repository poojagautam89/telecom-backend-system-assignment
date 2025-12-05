package com.telecom.api.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class InvoiceLineItemDTO {
    private String description;
    private BigDecimal amount;

    public InvoiceLineItemDTO() {}
    public InvoiceLineItemDTO(String description, BigDecimal amount){
        this.description = description;
        this.amount = amount;
    }
    public String getDescription(){ return description; }
    public void setDescription(String d){ this.description = d; }
    public BigDecimal getAmount(){ return amount; }
    public void setAmount(BigDecimal a){ this.amount = a; }
}
