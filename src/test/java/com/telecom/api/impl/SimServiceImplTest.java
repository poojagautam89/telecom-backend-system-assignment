package com.telecom.api.impl;

import com.telecom.api.dto.ActivateSimRequest;
import com.telecom.api.dto.SimResponseDTO;
import com.telecom.api.entity.Plan;
import com.telecom.api.entity.Sim;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.enum_pack.SimStatus;
import com.telecom.api.repository.PlanRepository;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.impl.SimServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimServiceImplTest {

    @Mock
    private SimRepository simRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private SimServiceImpl simService;

    private Subscriber subscriber;
    private Sim sim;
    private Plan plan;

    @BeforeEach
    void setup() {
        subscriber = new Subscriber();
        subscriber.setSubscriberId(UUID.randomUUID());
        subscriber.setFullName("Test User");

        plan = new Plan();
        plan.setPlanId(UUID.randomUUID());
        plan.setName("Basic Plan");

        sim = new Sim();
        sim.setSimId(UUID.randomUUID());
        sim.setImsi("imsi-123");
        sim.setMsisdn("9876543210");
        sim.setSubscriber(subscriber);
        sim.setPlan(plan);
        sim.setStatus(SimStatus.ACTIVE);
        sim.setActivationDate(LocalDate.now());
    }

    @Test
    void activateSim_success() {
        ActivateSimRequest req = new ActivateSimRequest();
        req.setSubscriberId(subscriber.getSubscriberId());
        req.setImsi("imsi-new");
        req.setMsisdn("1234567");

        when(subscriberRepository.findById(req.getSubscriberId())).thenReturn(Optional.of(subscriber));
        when(simRepository.save(any())).thenAnswer(inv -> {
            Sim savedSim = inv.getArgument(0);
            savedSim.setSimId(UUID.randomUUID());
            return savedSim;
        });

        SimResponseDTO dto = simService.activate(req);

        assertThat(dto.getImsi()).isEqualTo("imsi-new");
        assertThat(dto.getStatus()).isEqualTo(SimStatus.ACTIVE);
        assertThat(dto.getSubscriberId()).isEqualTo(subscriber.getSubscriberId());
    }

    @Test
    void activateSim_subscriberNotFound() {
        ActivateSimRequest req = new ActivateSimRequest();
        req.setSubscriberId(UUID.randomUUID());
        req.setImsi("imsi-test");

        when(subscriberRepository.findById(req.getSubscriberId())).thenReturn(Optional.empty());

        try {
            simService.activate(req);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Subscriber not found");
        }
    }

    @Test
    void assignPlan_success() {
        UUID simId = sim.getSimId();
        UUID planId = plan.getPlanId();

        when(simRepository.findById(simId)).thenReturn(Optional.of(sim));
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(simRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        simService.assignPlan(simId, planId);

        assertThat(sim.getPlan()).isEqualTo(plan);
    }

    @Test
    void blockSim_success() {
        UUID simId = sim.getSimId();

        when(simRepository.findById(simId)).thenReturn(Optional.of(sim));
        when(simRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        simService.block(simId, true);

        assertThat(sim.getStatus()).isEqualTo(SimStatus.BLOCKED);
    }

    @Test
    void getBySubscriber_success() {
        UUID subId = subscriber.getSubscriberId();

        when(simRepository.findBySubscriberSubscriberId(subId))
                .thenReturn(List.of(sim));

        List<SimResponseDTO> list = simService.getBySubscriber(subId);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getSubscriberId()).isEqualTo(subId);
    }
}
