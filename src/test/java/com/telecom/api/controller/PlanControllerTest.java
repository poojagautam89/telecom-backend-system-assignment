package com.telecom.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.api.dto.PlanRequestDTO;
import com.telecom.api.entity.Plan;
import com.telecom.api.enum_pack.PlanType;
import com.telecom.api.service.PlanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class)
@ExtendWith(MockitoExtension.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PlanService planService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPlan_returns201() throws Exception {
        PlanRequestDTO req = new PlanRequestDTO();
        req.setName("Gold");
        req.setType(PlanType.POSTPAID);
        req.setMonthlyPrice(BigDecimal.valueOf(199));
        req.setDataAllowanceMb(2048L);
        req.setCallAllowanceMin(500L);
        req.setSmsAllowance(1000L);

        Plan saved = new Plan();
        saved.setPlanId(UUID.randomUUID());
        saved.setName("Gold");

        Mockito.when(planService.create(any())).thenReturn(saved);

        mockMvc.perform(post("/api/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Gold"));
    }

    @Test
    void getPlan_returns200() throws Exception {
        UUID id = UUID.randomUUID();

        Plan plan = new Plan();
        plan.setPlanId(id);
        plan.setName("Silver");

        Mockito.when(planService.get(id)).thenReturn(plan);

        mockMvc.perform(get("/api/plans/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Silver"));
    }

    @Test
    void updatePlan_returns200() throws Exception {
        UUID id = UUID.randomUUID();

        PlanRequestDTO req = new PlanRequestDTO();
        req.setName("Diamond");
        req.setType(PlanType.PREPAID);
        req.setMonthlyPrice(BigDecimal.valueOf(299));
        req.setDataAllowanceMb(4048L);
        req.setCallAllowanceMin(800L);
        req.setSmsAllowance(1500L);

        Plan updated = new Plan();
        updated.setPlanId(id);
        updated.setName("Diamond");

        Mockito.when(planService.update(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/api/plans/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Diamond"));
    }

    @Test
    void getAllPlans_returns200() throws Exception {
        Plan p = new Plan();
        p.setPlanId(UUID.randomUUID());
        p.setName("Platinum");

        Mockito.when(planService.getAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Platinum"));
    }
}
