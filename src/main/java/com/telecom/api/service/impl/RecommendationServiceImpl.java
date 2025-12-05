package com.telecom.api.service.impl;

import com.telecom.api.dto.PlanRecommendationDTO;
import com.telecom.api.entity.Plan;
import com.telecom.api.entity.Sim;
import com.telecom.api.repository.PlanRepository;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.UsageRecordRepository;
import com.telecom.api.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final UsageRecordRepository usageRepo;
    private final PlanRepository planRepo;
    private final SimRepository simRepo;

    public RecommendationServiceImpl(UsageRecordRepository usageRepo, PlanRepository planRepo, SimRepository simRepo) {
        this.usageRepo = usageRepo;
        this.planRepo = planRepo;
        this.simRepo = simRepo;
    }

    @Override
    public PlanRecommendationDTO recommendPlan(UUID subscriberId) {
        // naive algorithm: compute last 3 months average usage across all sims
        YearMonth now = YearMonth.now();
        YearMonth threeMonths = now.minusMonths(2);
        LocalDateTime start = threeMonths.atDay(1).atStartOfDay();
        LocalDateTime end = now.atEndOfMonth().atTime(23,59,59);

        long totalDataKb = 0L, totalCall = 0L, totalSms = 0L;
        List<Sim> sims = simRepo.findBySubscriberSubscriberId(subscriberId);
        for (Sim sim : sims) {
            Object[] totals = usageRepo.aggregateTotalsBySim(sim.getSimId(), start, end);
            if (totals != null) {
                totalDataKb += totals[0] != null ? ((Number)totals[0]).longValue() : 0L;
                totalCall += totals[1] != null ? ((Number)totals[1]).longValue() : 0L;
                totalSms += totals[2] != null ? ((Number)totals[2]).longValue() : 0L;
            }
        }
        long months = 3;
        long avgDataMb = (int) ((totalDataKb / 1024) / months); // MB
        long avgCallMin = totalCall / months;
        long avgSms = totalSms / months;

        Plan best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Plan p : planRepo.findAll()) {
            // score: plans that cover usage get higher score, lower price gives bonus
            double score = 0;
            if (p.getDataAllowanceMb() >= avgDataMb) score += 50;
            else score -= (avgDataMb - p.getDataAllowanceMb()) / 100.0;

            if (p.getCallAllowanceMin() >= avgCallMin) score += 20;
            else score -= (avgCallMin - p.getCallAllowanceMin()) / 10.0;

            if (p.getSmsAllowance() >= avgSms) score += 10;
            else score -= (avgSms - p.getSmsAllowance()) / 5.0;

            // price penalty
            double price = p.getMonthlyPrice() != null ? p.getMonthlyPrice().doubleValue() : 0;
            score -= price / 100.0;

            if (score > bestScore) { bestScore = score; best = p; }
        }

        PlanRecommendationDTO dto = new PlanRecommendationDTO();
        if (best != null) {
            dto.setRecommendedPlanId(best.getPlanId());
            dto.setPlanName(best.getName());
            dto.setReason("Based on avg usage: data " + avgDataMb + "MB / month, calls " + avgCallMin + " min, sms " + avgSms);
            dto.setEstimatedMonthlyCost(best.getMonthlyPrice());
        } else {
            dto.setReason("No plan data available");
        }
        return dto;
    }
}
