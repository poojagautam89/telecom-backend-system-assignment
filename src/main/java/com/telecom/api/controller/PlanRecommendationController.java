package com.telecom.api.controller;

import com.telecom.api.dto.PlanRecommendationDTO;
import com.telecom.api.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/plan")
public class PlanRecommendationController {

    private final RecommendationService recommendationService;

    public PlanRecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
    // USER + ADMIN allowed (read-only)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/recommend/{subscriberId}")
    public ResponseEntity<PlanRecommendationDTO> recommend(@PathVariable UUID subscriberId) {
        return ResponseEntity.ok(recommendationService.recommendPlan(subscriberId));
    }
}
