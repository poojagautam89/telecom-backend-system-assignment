package com.telecom.api.service.impl;

import com.telecom.api.dto.UsageRequest;
import com.telecom.api.entity.Sim;
import com.telecom.api.entity.UsageRecord;
import com.telecom.api.enum_pack.UsageType;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.UsageRecordRepository;
import com.telecom.api.service.UsageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UsageServiceImpl implements UsageService {

    private final UsageRecordRepository usageRepo;
    private final SimRepository simRepo;

    public UsageServiceImpl(UsageRecordRepository usageRepo, SimRepository simRepo) {
        this.usageRepo = usageRepo;
        this.simRepo = simRepo;
    }

    @Override
    @Transactional
    public void saveUsage(UsageRequest request) {
        // validate sim exists
        Sim sim = simRepo.findById(request.getSimId())
                .orElseThrow(() -> new IllegalArgumentException("SIM not found"));

        UsageRecord ur = UsageRecord.builder()
                .sim(sim)
                .usageType(UsageType.valueOf(request.getType()))
                .usageValue(request.getValue())
                .usageTimestamp(request.getTimestamp())
                .createdAt(LocalDateTime.now())
                .build();

        usageRepo.save(ur);
    }
}
