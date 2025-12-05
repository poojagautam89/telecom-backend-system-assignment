package com.telecom.api.dto;

import java.util.UUID;
import java.math.BigDecimal;

public class PlanRecommendationDTO {
    private UUID recommendedPlanId;
    private String planName;
    private String reason;
    private BigDecimal estimatedMonthlyCost;

    // getters/setters
    public UUID getRecommendedPlanId() { return recommendedPlanId; }
    public void setRecommendedPlanId(UUID recommendedPlanId) { this.recommendedPlanId = recommendedPlanId; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public BigDecimal getEstimatedMonthlyCost() { return estimatedMonthlyCost; }
    public void setEstimatedMonthlyCost(BigDecimal estimatedMonthlyCost) { this.estimatedMonthlyCost = estimatedMonthlyCost; }
}
