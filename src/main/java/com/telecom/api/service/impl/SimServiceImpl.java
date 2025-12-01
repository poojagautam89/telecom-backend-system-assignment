package com.telecom.api.service.impl;

import com.telecom.api.dto.ActivateSimRequest;
import com.telecom.api.dto.SimResponseDTO;
import com.telecom.api.entity.Plan;
import com.telecom.api.entity.Sim;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.enum_pack.SimStatus;
import com.telecom.api.repository.PlanRepository;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.SimService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SimServiceImpl implements SimService {

    private final SimRepository simRepository;
    private final SubscriberRepository subscriberRepository;
    private final PlanRepository planRepository;

    public SimServiceImpl(SimRepository simRepository,
                          SubscriberRepository subscriberRepository,
                          PlanRepository planRepository) {
        this.simRepository = simRepository;
        this.subscriberRepository = subscriberRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public SimResponseDTO activate(ActivateSimRequest req) {
        // validate subscriber exists
        Subscriber subscriber = subscriberRepository.findById(req.getSubscriberId())
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found: " + req.getSubscriberId()));

        // create sim and set active + activationDate
        Sim sim = Sim.builder()
                .imsi(req.getImsi())
                .msisdn(req.getMsisdn())
                .subscriber(subscriber)
                .status(SimStatus.ACTIVE)
                .activationDate(LocalDate.now())
                .build();

        Sim saved = simRepository.save(sim);
        return toDto(saved);
    }

    @Override
    @Transactional
    public void block(UUID simId, boolean block) {
        Sim sim = simRepository.findById(simId).orElseThrow(() -> new IllegalArgumentException("SIM not found: " + simId));
        sim.setStatus(block ? SimStatus.BLOCKED : SimStatus.ACTIVE);
        simRepository.save(sim);
    }

    @Override
    @Transactional
    public void assignPlan(UUID simId, UUID planId) {
        Sim sim = simRepository.findById(simId).orElseThrow(() -> new IllegalArgumentException("SIM not found: " + simId));
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
        sim.setPlan(plan);
        simRepository.save(sim);
    }

    @Override
    public List<SimResponseDTO> getBySubscriber(UUID subscriberId) {
        return simRepository.findBySubscriberSubscriberId(subscriberId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SimResponseDTO toDto(Sim sim) {
        SimResponseDTO dto = new SimResponseDTO();
        dto.setSimId(sim.getSimId());
        dto.setImsi(sim.getImsi());
        dto.setMsisdn(sim.getMsisdn());
        dto.setSubscriberId(sim.getSubscriber() != null ? sim.getSubscriber().getSubscriberId() : null);
        dto.setPlanId(sim.getPlan() != null ? sim.getPlan().getPlanId() : null);
        dto.setStatus(sim.getStatus());
        return dto;
    }
}
