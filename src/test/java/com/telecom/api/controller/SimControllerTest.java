package com.telecom.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.api.dto.ActivateSimRequest;
import com.telecom.api.dto.AssignPlanRequest;
import com.telecom.api.dto.SimResponseDTO;
import com.telecom.api.enum_pack.SimStatus;
import com.telecom.api.service.SimService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimController.class)
@ExtendWith(MockitoExtension.class)
class SimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private SimService simService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void activateSim_returns201() throws Exception {
        ActivateSimRequest req = new ActivateSimRequest();
        req.setSubscriberId(UUID.randomUUID());
        req.setImsi("imsi-xyz");
        req.setMsisdn("999999");

        SimResponseDTO dto = new SimResponseDTO();
        dto.setSimId(UUID.randomUUID());
        dto.setImsi("imsi-xyz");
        dto.setStatus(SimStatus.ACTIVE);
        dto.setSubscriberId(req.getSubscriberId());

        Mockito.when(simService.activate(any())).thenReturn(dto);

        mockMvc.perform(post("/api/sim/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imsi").value("imsi-xyz"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void blockSim_returns204() throws Exception {
        UUID simId = UUID.randomUUID();

        mockMvc.perform(put("/api/sim/" + simId + "/block")
                        .param("block", "true"))
                .andExpect(status().isNoContent());

        Mockito.verify(simService).block(eq(simId), eq(true));
    }

    @Test
    void assignPlan_returns200() throws Exception {
        UUID simId = UUID.randomUUID();
        AssignPlanRequest req = new AssignPlanRequest();
        req.setPlanId(UUID.randomUUID());

        mockMvc.perform(put("/api/sim/" + simId + "/assign-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Mockito.verify(simService).assignPlan(eq(simId), eq(req.getPlanId()));
    }

    @Test
    void getBySubscriber_returnsList() throws Exception {
        UUID subId = UUID.randomUUID();

        SimResponseDTO dto = new SimResponseDTO();
        dto.setSimId(UUID.randomUUID());
        dto.setSubscriberId(subId);
        dto.setImsi("imsi-test");

        Mockito.when(simService.getBySubscriber(subId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/sim/subscriber/" + subId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].imsi").value("imsi-test"));
    }
}
