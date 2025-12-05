package com.telecom.api.impl;

import com.telecom.api.repository.PlanRepository;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.UsageRecordRepository;
import com.telecom.api.service.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceImplTest {

    @Mock UsageRecordRepository usageRepo;
    @Mock PlanRepository planRepo;
    @Mock SimRepository simRepo;
    @InjectMocks RecommendationServiceImpl recommendationService;

    @Test
    void recommend_noSims_returnsReason() {
        UUID sub = UUID.randomUUID();
        when(simRepo.findBySubscriberSubscriberId(sub)).thenReturn(List.of());
        var dto = recommendationService.recommendPlan(sub);
        assertThat(dto.getReason()).contains("No plan data", "No plan");
    }
}
