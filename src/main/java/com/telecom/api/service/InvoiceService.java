package com.telecom.api.service;

import com.telecom.api.dto.InvoiceResponseDTO;

public interface InvoiceService {
    InvoiceResponseDTO getInvoice(Long billId);
    byte[] generateInvoicePdf(Long billId);
}
