package com.telecom.api.service;

import com.telecom.api.entity.Plan;
import java.util.List;
import java.util.UUID;

public interface PlanService {
    Plan create(Plan plan);
    Plan update(UUID planId, Plan plan);
    void delete(UUID planId);
    Plan get(UUID planId);
    List<Plan> getAll();
}
