package com.telecom.api.service;

import com.telecom.api.dto.ActivateSimRequest;
import com.telecom.api.dto.SimResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SimService {
    SimResponseDTO activate(ActivateSimRequest req);
    void block(UUID simId, boolean block);
    void assignPlan(UUID simId, UUID planId);
    List<SimResponseDTO> getBySubscriber(UUID subscriberId);
}
