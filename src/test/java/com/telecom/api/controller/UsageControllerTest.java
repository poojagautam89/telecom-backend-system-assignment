package com.telecom.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.api.dto.UsageRequest;
import com.telecom.api.service.UsageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsageController.class)
@ExtendWith(MockitoExtension.class)
class UsageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UsageService usageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveUsage_returns201() throws Exception {
        UsageRequest req = new UsageRequest();
        req.setSimId(UUID.randomUUID());
        req.setType("DATA");
        req.setValue(1024L);
        req.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/api/usage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
