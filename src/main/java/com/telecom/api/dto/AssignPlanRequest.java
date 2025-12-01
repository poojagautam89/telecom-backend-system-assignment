package com.telecom.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AssignPlanRequest {
    @NotNull
    private UUID planId;

    public UUID getPlanId() { return planId; }
    public void setPlanId(UUID planId) { this.planId = planId; }
}
