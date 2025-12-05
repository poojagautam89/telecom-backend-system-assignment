package com.telecom.api.service;

import com.telecom.api.dto.PlanRecommendationDTO;
import java.util.UUID;

public interface RecommendationService {
    PlanRecommendationDTO recommendPlan(UUID subscriberId);
}
