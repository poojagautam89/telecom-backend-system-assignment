package com.telecom.api.controller;

import com.telecom.api.dto.UsageDayDTO;
import com.telecom.api.dto.UsageSummaryDTO;
import com.telecom.api.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usage/report")
public class UsageReportController {

    private final ReportService reportService;

    public UsageReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // USER + ADMIN (read-only)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/daily/{simId}")
    public ResponseEntity<List<UsageDayDTO>> daily(@PathVariable UUID simId,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(reportService.getDailyUsage(simId, date));
    }

    // USER + ADMIN (read-only)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/monthly/{simId}")
    public ResponseEntity<UsageSummaryDTO> monthly(@PathVariable UUID simId,
                                                   @RequestParam(required = false) YearMonth month) {
        if (month == null) month = YearMonth.now();
        return ResponseEntity.ok(reportService.getMonthlyUsage(simId, month));
    }

    // USER + ADMIN (read-only)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/summary/{subscriberId}")
    public ResponseEntity<UsageSummaryDTO> summary(@PathVariable UUID subscriberId,
                                                   @RequestParam(required = false) YearMonth month) {
        if (month == null) month = YearMonth.now();
        return ResponseEntity.ok(reportService.getSubscriberSummary(subscriberId, month));
    }
}
