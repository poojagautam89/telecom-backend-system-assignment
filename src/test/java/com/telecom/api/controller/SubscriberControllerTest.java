package com.telecom.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;
import com.telecom.api.service.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SubscriberControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SubscriberService service;

    @InjectMocks
    private SubscriberController controller;

    @BeforeEach
    void setUp() {
        // standaloneSetup avoids loading full Spring context and does not need @MockBean
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.telecom.api.exception.GlobalExceptionHandler()) // optional: include exception handler
                .build();
    }

    @Test
    void createAndGetSubscriber() throws Exception {
        UUID id = UUID.randomUUID();

        SubscriberRequestDTO req = new SubscriberRequestDTO();
        req.setFullName("Pooja");
        req.setEmail("p@example.com");
        req.setPhoneNumber("9999999999");
        req.setAddress("Delhi");

        SubscriberResponseDTO res = new SubscriberResponseDTO();
        res.setSubscriberId(id);
        res.setFullName(req.getFullName());
        res.setEmail(req.getEmail());
        res.setPhoneNumber(req.getPhoneNumber());
        res.setAddress(req.getAddress());
        res.setActivationDate(LocalDate.now());
        res.setStatus("ACTIVE");

        Mockito.when(service.createSubscriber(Mockito.any())).thenReturn(res);
        Mockito.when(service.getSubscriber(id)).thenReturn(res);

        mockMvc.perform(post("/api/subscribers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriberId").value(id.toString()))
                .andExpect(jsonPath("$.fullName").value("Pooja"));

        mockMvc.perform(get("/api/subscribers/" + id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriberId").value(id.toString()))
                .andExpect(jsonPath("$.email").value("p@example.com"));
    }
}
