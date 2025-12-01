

package com.telecom.api.service.impl;

import com.telecom.api.entity.*;
import com.telecom.api.repository.*;
import com.telecom.api.service.BillingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillingServiceImpl implements BillingService {

    private final UsageRecordRepository usageRepo;
    private final SimRepository simRepo;
    private final BillRepository billRepo;
    private final PlanRepository planRepo;

    // rates (client)
    private static final BigDecimal RATE_PER_GB = new BigDecimal("10.00"); // ₹10 per GB
    private static final BigDecimal RATE_PER_SMS = new BigDecimal("0.5");
    private static final BigDecimal RATE_PER_MIN = new BigDecimal("1.0");

    public BillingServiceImpl(UsageRecordRepository usageRepo,
                              SimRepository simRepo,
                              BillRepository billRepo,
                              PlanRepository planRepo) {
        this.usageRepo = usageRepo;
        this.simRepo = simRepo;
        this.billRepo = billRepo;
        this.planRepo = planRepo;
    }

    @Override
    @Transactional
    public Bill generateBill(UUID simId, YearMonth month) {
        Sim sim = simRepo.findById(simId).orElseThrow(() -> new IllegalArgumentException("SIM not found"));
        Plan plan = sim.getPlan();

        BigDecimal baseAmount = (plan != null && plan.getMonthlyPrice() != null) ? plan.getMonthlyPrice() : BigDecimal.ZERO;

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23,59,59);

        List<UsageRecord> usages = usageRepo.findBySimSimIdAndUsageTimestampBetween(simId, start, end);

        long totalCallMin = usages.stream()
                .filter(u -> u.getUsageType().name().equals("CALL"))
                .mapToLong(UsageRecord::getUsageValue).sum();

        long totalDataKb = usages.stream()
                .filter(u -> u.getUsageType().name().equals("DATA"))
                .mapToLong(UsageRecord::getUsageValue).sum();

        long totalSms = usages.stream()
                .filter(u -> u.getUsageType().name().equals("SMS"))
                .mapToLong(UsageRecord::getUsageValue).sum();

        // Convert usage units:
        // totalDataKb -> GB = KB / (1024*1024)
        BigDecimal totalDataGb = BigDecimal.valueOf(totalDataKb)
                .divide(BigDecimal.valueOf(1024L * 1024L), 6, RoundingMode.HALF_UP);

        long planDataMb = (plan != null && plan.getDataAllowanceMb() != null) ? plan.getDataAllowanceMb() : 0L;
        BigDecimal planDataGb = BigDecimal.valueOf(planDataMb)
                .divide(BigDecimal.valueOf(1024L), 6, RoundingMode.HALF_UP);

        BigDecimal extraDataGb = totalDataGb.subtract(planDataGb).max(BigDecimal.ZERO);

        long extraCallMin = Math.max(0, totalCallMin - ((plan != null && plan.getCallAllowanceMin()!=null) ? plan.getCallAllowanceMin().intValue() : 0));
        long extraSms = Math.max(0, totalSms - ((plan != null && plan.getSmsAllowance()!=null) ? plan.getSmsAllowance().intValue() : 0));

        BigDecimal dataCharge = extraDataGb.multiply(RATE_PER_GB).setScale(2, RoundingMode.HALF_UP);
        BigDecimal callCharge = BigDecimal.valueOf(extraCallMin).multiply(RATE_PER_MIN).setScale(2, RoundingMode.HALF_UP);
        BigDecimal smsCharge = BigDecimal.valueOf(extraSms).multiply(RATE_PER_SMS).setScale(2, RoundingMode.HALF_UP);

        BigDecimal overage = dataCharge.add(callCharge).add(smsCharge);
        BigDecimal total = baseAmount.add(overage);

        String monthStr = month.toString(); // YYYY-MM

        // upsert bill
        Bill bill = billRepo.findBySimSimIdAndBillingMonth(simId, monthStr).orElse(new Bill());
        bill.setSim(sim);
        bill.setBillingMonth(monthStr);
        bill.setBaseAmount(baseAmount);
        bill.setOverageAmount(overage);
        bill.setTotalAmount(total);
        bill.setGeneratedAt(LocalDateTime.now());
        billRepo.save(bill);

        return bill;
    }
}

