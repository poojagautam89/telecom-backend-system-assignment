package com.telecom.api.impl;

import com.telecom.api.entity.Plan;
import com.telecom.api.enum_pack.PlanType;
import com.telecom.api.repository.PlanRepository;
import com.telecom.api.service.impl.PlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanServiceImpl planService;

    private Plan samplePlan;

    @BeforeEach
    void setUp() {
        samplePlan = Plan.builder()
                .planId(UUID.randomUUID())
                .name("Gold")
                .type(PlanType.POSTPAID)
                .monthlyPrice(BigDecimal.valueOf(299))
                .dataAllowanceMb(2048L)
                .callAllowanceMin(500L)
                .smsAllowance(1000L)
                .build();
    }

    @Test
    void createPlan_success() {
        when(planRepository.save(any())).thenAnswer(inv -> {
            Plan p = inv.getArgument(0);
            if (p.getPlanId() == null) p.setPlanId(UUID.randomUUID());
            return p;
        });

        Plan created = planService.create(samplePlan);

        assertThat(created).isNotNull();
        assertThat(created.getPlanId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Gold");
        verify(planRepository, times(1)).save(samplePlan);
    }

    @Test
    void updatePlan_success() {
        UUID id = samplePlan.getPlanId();
        Plan updateInfo = new Plan();
        updateInfo.setName("Diamond");
        updateInfo.setType(PlanType.PREPAID);
        updateInfo.setMonthlyPrice(BigDecimal.valueOf(499));
        updateInfo.setDataAllowanceMb(4096L);
        updateInfo.setCallAllowanceMin(1000L);
        updateInfo.setSmsAllowance(2000L);

        when(planRepository.findById(id)).thenReturn(Optional.of(samplePlan));
        when(planRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Plan updated = planService.update(id, updateInfo);

        assertThat(updated.getName()).isEqualTo("Diamond");
        assertThat(updated.getType()).isEqualTo(PlanType.PREPAID);
        assertThat(updated.getMonthlyPrice()).isEqualByComparingTo("499");
    }

    @Test
    void updatePlan_notFound() {
        UUID id = UUID.randomUUID();
        when(planRepository.findById(id)).thenReturn(Optional.empty());

        try {
            planService.update(id, samplePlan);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Plan not found");
        }
    }

    @Test
    void deletePlan_success() {
        UUID id = samplePlan.getPlanId();
        doNothing().when(planRepository).deleteById(id);

        planService.delete(id);

        verify(planRepository, times(1)).deleteById(id);
    }

    @Test
    void getPlan_success() {
        UUID id = samplePlan.getPlanId();
        when(planRepository.findById(id)).thenReturn(Optional.of(samplePlan));

        Plan result = planService.get(id);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Gold");
    }

    @Test
    void getAllPlans_success() {
        when(planRepository.findAll()).thenReturn(List.of(samplePlan, samplePlan));

        List<Plan> list = planService.getAll();

        assertThat(list).hasSize(2);
    }
}
