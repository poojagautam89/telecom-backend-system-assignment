package com.telecom.api.impl;

import com.telecom.api.entity.*;
import com.telecom.api.enum_pack.UsageType;
import com.telecom.api.repository.*;
import com.telecom.api.service.impl.BillingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceImplTest {

    @Mock
    private UsageRecordRepository usageRepo;

    @Mock
    private SimRepository simRepo;

    @Mock
    private BillRepository billRepo;

    @Mock
    private PlanRepository planRepo;

    @InjectMocks
    private BillingServiceImpl billingService;

    private UUID simId;
    private Sim sim;
    private Plan plan;

    @BeforeEach
    void setUp() {
        simId = UUID.randomUUID();

        plan = new Plan();
        plan.setPlanId(UUID.randomUUID());
        plan.setName("TestPlan");
        plan.setMonthlyPrice(BigDecimal.valueOf(100)); // base

        // plan allowances: 1 GB = 1024 MB
        plan.setDataAllowanceMb(1024L); // 1 GB
        plan.setCallAllowanceMin(100L);
        plan.setSmsAllowance(50L);

        sim = new Sim();
        sim.setSimId(simId);
        sim.setImsi("imsi-001");
        sim.setPlan(plan);
    }

    @Test
    void generateBill_calculatesCorrectCharges() {
        // Prepare usages for the month: 3 GB data, 200 mins calls, 60 sms
        // Data stored in KB: 3 * 1024 * 1024 KB
        long threeGbKb = 3L * 1024L * 1024L;
        UsageRecord dataUsage = new UsageRecord();
        dataUsage.setId(null);
        dataUsage.setSim(sim);
        dataUsage.setUsageType(com.telecom.api.enum_pack.UsageType.DATA);
        dataUsage.setUsageValue(threeGbKb);
        dataUsage.setUsageTimestamp(LocalDateTime.now());

        UsageRecord callUsage = new UsageRecord();
        callUsage.setSim(sim);
        callUsage.setUsageType(com.telecom.api.enum_pack.UsageType.CALL);
        callUsage.setUsageValue(200L); // minutes
        callUsage.setUsageTimestamp(LocalDateTime.now());

        UsageRecord smsUsage = new UsageRecord();
        smsUsage.setSim(sim);
        smsUsage.setUsageType(com.telecom.api.enum_pack.UsageType.SMS);
        smsUsage.setUsageValue(60L);
        smsUsage.setUsageTimestamp(LocalDateTime.now());

        YearMonth ym = YearMonth.now();
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        when(simRepo.findById(simId)).thenReturn(Optional.of(sim));
        when(usageRepo.findBySimSimIdAndUsageTimestampBetween(simId, start, end))
                .thenReturn(List.of(dataUsage, callUsage, smsUsage));
        when(billRepo.findBySimSimIdAndBillingMonth(simId, ym.toString())).thenReturn(Optional.empty());
        when(billRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Execute
        Bill bill = billingService.generateBill(simId, ym);

        // Validate: base = 100
        assertThat(bill.getBaseAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));

        // Expected overage:
        // Data: plan 1GB, used 3GB => extra 2GB * 10 = 20
        // Call: plan 100, used 200 => extra 100 * 1 = 100
        // SMS: plan 50, used 60 => extra 10 * 0.5 = 5
        BigDecimal expectedOverage = BigDecimal.valueOf(20 + 100 + 5).setScale(2);
        BigDecimal expectedTotal = BigDecimal.valueOf(100).add(expectedOverage).setScale(2);

        assertThat(bill.getOverageAmount().setScale(2, BigDecimal.ROUND_HALF_UP))
                .isEqualByComparingTo(expectedOverage);
        assertThat(bill.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP))
                .isEqualByComparingTo(expectedTotal);

        verify(billRepo, times(1)).save(any(Bill.class));
    }

    @Test
    void generateBill_whenSimNotFound_throws() {
        YearMonth ym = YearMonth.now();
        when(simRepo.findById(simId)).thenReturn(Optional.empty());

        try {
            billingService.generateBill(simId, ym);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("SIM not found");
        }

        verify(usageRepo, never()).findBySimSimIdAndUsageTimestampBetween(any(), any(), any());
        verify(billRepo, never()).save(any());
    }
}
