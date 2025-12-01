package com.telecom.api.service.impl;

import com.telecom.api.entity.Plan;
import com.telecom.api.repository.PlanRepository;
import com.telecom.api.service.PlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepo;

    public PlanServiceImpl(PlanRepository planRepo) {
        this.planRepo = planRepo;
    }

    @Override
    @Transactional
    public Plan create(Plan plan) {
        return planRepo.save(plan);
    }

    @Override
    @Transactional
    public Plan update(UUID planId, Plan plan) {
        Plan existing = planRepo.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        existing.setName(plan.getName());
        existing.setType(plan.getType());
        existing.setMonthlyPrice(plan.getMonthlyPrice());
        existing.setDataAllowanceMb(plan.getDataAllowanceMb());
        existing.setCallAllowanceMin(plan.getCallAllowanceMin());
        existing.setSmsAllowance(plan.getSmsAllowance());
        return planRepo.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID planId) {
        planRepo.deleteById(planId);
    }

    @Override
    public Plan get(UUID planId) {
        return planRepo.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found"));
    }

    @Override
    public List<Plan> getAll() {
        return planRepo.findAll();
    }
}
