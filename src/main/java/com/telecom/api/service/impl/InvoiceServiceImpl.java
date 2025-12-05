package com.telecom.api.service.impl;

import com.telecom.api.dto.InvoiceLineItemDTO;
import com.telecom.api.dto.InvoiceResponseDTO;
import com.telecom.api.entity.Bill;
import com.telecom.api.entity.Sim;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.repository.BillRepository;
import com.telecom.api.service.InvoiceService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final BillRepository billRepo;

    public InvoiceServiceImpl(BillRepository billRepo) {
        this.billRepo = billRepo;
    }

    @Override
    public InvoiceResponseDTO getInvoice(Long billId) {
        Bill bill = billRepo.findById(billId).orElseThrow(() -> new IllegalArgumentException("Bill not found: " + billId));
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setBillId(bill.getId());
        dto.setBillingMonth(bill.getBillingMonth());
        dto.setBaseAmount(bill.getBaseAmount());
        dto.setOverageAmount(bill.getOverageAmount());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setGeneratedAt(bill.getGeneratedAt());
        // subscriber & sim info (best-effort)
        Sim sim = bill.getSim();
        if (sim != null) {
            dto.setMsisdn(sim.getMsisdn());
            Subscriber sub = sim.getSubscriber();
            if (sub != null) {
                dto.setSubscriberName(sub.getFullName());
                dto.setSubscriberEmail(sub.getEmail());
            }
        }
        // line items
        List<InvoiceLineItemDTO> items = new ArrayList<>();
        items.add(new InvoiceLineItemDTO("Base amount", bill.getBaseAmount() != null ? bill.getBaseAmount() : BigDecimal.ZERO));
        items.add(new InvoiceLineItemDTO("Overage charges", bill.getOverageAmount() != null ? bill.getOverageAmount() : BigDecimal.ZERO));
        dto.setItems(items);
        return dto;
    }

    @Override
    public byte[] generateInvoicePdf(Long billId) {
        InvoiceResponseDTO inv = getInvoice(billId);
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);

            float y = 800;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
            cs.newLineAtOffset(50, y);
            cs.showText("Telecom Company - Invoice");
            cs.endText();

            y -= 30;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 11);
            cs.newLineAtOffset(50, y);
            cs.showText("Bill ID: " + inv.getBillId());
            cs.endText();

            y -= 15;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 11);
            cs.newLineAtOffset(50, y);
            cs.showText("Billing Month: " + inv.getBillingMonth());
            cs.endText();

            y -= 20;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(50, y);
            cs.showText("Subscriber:");
            cs.endText();

            y -= 15;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 11);
            cs.newLineAtOffset(60, y);
            cs.showText(inv.getSubscriberName() + " | " + inv.getSubscriberEmail());
            cs.endText();

            y -= 20;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(50, y);
            cs.showText("Line Items:");
            cs.endText();

            y -= 15;
            for (InvoiceLineItemDTO item : inv.getItems()) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(60, y);
                String desc = item.getDescription();
                String amt = item.getAmount() != null ? item.getAmount().toPlainString() : "0";
                cs.showText(desc + " : ₹ " + amt);
                cs.endText();
                y -= 15;
            }

            y -= 10;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(50, y);
            cs.showText("Total: ₹ " + (inv.getTotalAmount() != null ? inv.getTotalAmount().toPlainString() : "0"));
            cs.endText();

            y -= 25;
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 9);
            cs.newLineAtOffset(50, y);
            cs.showText("Generated at: " + (inv.getGeneratedAt() != null ? inv.getGeneratedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : ""));
            cs.endText();

            cs.close();
            doc.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PDF", e);
        }
    }
}
