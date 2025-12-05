package com.telecom.api.service;

import com.telecom.api.dto.UsageDayDTO;
import com.telecom.api.dto.UsageSummaryDTO;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReportService {
    List<UsageDayDTO> getDailyUsage(UUID simId, LocalDate date); // returns days (for that month or single day)
    UsageSummaryDTO getMonthlyUsage(UUID simId, YearMonth month);
    UsageSummaryDTO getSubscriberSummary(UUID subscriberId, YearMonth month);
}
