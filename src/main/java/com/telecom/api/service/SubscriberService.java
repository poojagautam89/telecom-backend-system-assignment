package com.telecom.api.service;

import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SubscriberService {
    SubscriberResponseDTO createSubscriber(SubscriberRequestDTO request);
    SubscriberResponseDTO getSubscriber(UUID id);
    List<SubscriberResponseDTO> listSubscribers(String status);
    SubscriberResponseDTO updateSubscriber(UUID id, SubscriberRequestDTO request);
    void deleteSubscriber(UUID id);
}
