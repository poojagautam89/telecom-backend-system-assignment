package com.telecom.api.service.impl;

import com.telecom.api.dto.UsageDayDTO;
import com.telecom.api.dto.UsageSummaryDTO;
import com.telecom.api.entity.Sim;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.repository.UsageRecordRepository;
import com.telecom.api.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    private final UsageRecordRepository usageRepo;
    private final SimRepository simRepo;
    private final SubscriberRepository subscriberRepo;

    public ReportServiceImpl(UsageRecordRepository usageRepo, SimRepository simRepo, SubscriberRepository subscriberRepo) {
        this.usageRepo = usageRepo;
        this.simRepo = simRepo;
        this.subscriberRepo = subscriberRepo;
    }

    @Override
    public List<UsageDayDTO> getDailyUsage(UUID simId, LocalDate date) {
        // we will return daily aggregates for the month containing 'date'
        YearMonth ym = YearMonth.from(date);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23,59,59);

        // usageRepo.aggregateDailyBySim should return List<Object[]> where:
        // r[0] = LocalDate (day), r[1] = dataKb, r[2] = callMin, r[3] = smsCount
        List<Object[]> rows = usageRepo.aggregateDailyBySim(simId, start, end);
        if (rows == null) {
            return Collections.emptyList();
        }

        List<UsageDayDTO> out = new ArrayList<>();
        for (Object[] r : rows) {
            String day = r[0] != null ? r[0].toString() : "";
            Long dataKb = r[1] != null ? ((Number) r[1]).longValue() : 0L;
            Long callMin = r[2] != null ? ((Number) r[2]).longValue() : 0L;
            Long sms = r[3] != null ? ((Number) r[3]).longValue() : 0L;

            UsageDayDTO dto = new UsageDayDTO();
            dto.setDay(day);
            dto.setDataKb(dataKb);
            dto.setCallMin(callMin);
            dto.setSmsCount(sms);
            out.add(dto);
        }
        return out;
    }

    @Override
    public UsageSummaryDTO getMonthlyUsage(UUID simId, YearMonth month) {
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23,59,59);

        // usageRepo.aggregateTotalsBySim should return Object[]: [dataKb, callMin, sms]
        Object[] totals = usageRepo.aggregateTotalsBySim(simId, start, end);
        Long dataKb = 0L;
        Long callMin = 0L;
        Long sms = 0L;
        if (totals != null) {
            dataKb = totals[0] != null ? ((Number) totals[0]).longValue() : 0L;
            callMin = totals[1] != null ? ((Number) totals[1]).longValue() : 0L;
            sms = totals[2] != null ? ((Number) totals[2]).longValue() : 0L;
        }

        UsageSummaryDTO summary = new UsageSummaryDTO();
        summary.setTotalDataKb(dataKb);
        summary.setTotalCallMin(callMin);
        summary.setTotalSms(sms);
        return summary;
    }

    @Override
    public UsageSummaryDTO getSubscriberSummary(UUID subscriberId, YearMonth month) {
        // verify subscriber exists
        Subscriber s = subscriberRepo.findById(subscriberId)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found: " + subscriberId));

        // Get all sims for subscriber
        List<Sim> sims = simRepo.findBySubscriberSubscriberId(subscriberId);
        if (sims == null || sims.isEmpty()) {
            // return zero summary
            UsageSummaryDTO empty = new UsageSummaryDTO();
            empty.setTotalDataKb(0L);
            empty.setTotalCallMin(0L);
            empty.setTotalSms(0L);
            empty.setDays(Collections.emptyList());
            return empty;
        }

        long totalData = 0L;
        long totalCalls = 0L;
        long totalSms = 0L;

        for (Sim sim : sims) {
            UsageSummaryDTO perSim = getMonthlyUsage(sim.getSimId(), month);
            totalData += perSim.getTotalDataKb() != null ? perSim.getTotalDataKb() : 0L;
            totalCalls += perSim.getTotalCallMin() != null ? perSim.getTotalCallMin() : 0L;
            totalSms += perSim.getTotalSms() != null ? perSim.getTotalSms() : 0L;
        }

        UsageSummaryDTO result = new UsageSummaryDTO();
        result.setTotalDataKb(totalData);
        result.setTotalCallMin(totalCalls);
        result.setTotalSms(totalSms);
        result.setDays(Collections.emptyList());
        return result;
    }
}
