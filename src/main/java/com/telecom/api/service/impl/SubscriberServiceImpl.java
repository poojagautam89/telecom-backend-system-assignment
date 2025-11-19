package com.telecom.api.service.impl;

import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.SubscriberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository repo;

    public SubscriberServiceImpl(SubscriberRepository repo) {
        this.repo = repo;
    }

    @Override
    public SubscriberResponseDTO createSubscriber(SubscriberRequestDTO request) {
        Subscriber s = Subscriber.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();
        repo.save(s);
        return toDto(s);
    }

    @Override
    public SubscriberResponseDTO getSubscriber(UUID id) {
        Subscriber s = repo.findById(id).orElseThrow(() -> new RuntimeException("Subscriber not found"));
        return toDto(s);
    }

    @Override
    public List<SubscriberResponseDTO> listSubscribers(String status) {
        List<Subscriber> list = (status == null || status.isBlank())
                ? repo.findAll()
                : repo.findByStatus(status);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public SubscriberResponseDTO updateSubscriber(UUID id, SubscriberRequestDTO request) {
        Subscriber s = repo.findById(id).orElseThrow(() -> new RuntimeException("Subscriber not found"));
        s.setFullName(request.getFullName());
        s.setEmail(request.getEmail());
        s.setPhoneNumber(request.getPhoneNumber());
        s.setAddress(request.getAddress());
        repo.save(s);
        return toDto(s);
    }

    @Override
    public void deleteSubscriber(UUID id) {
        repo.deleteById(id);
    }

    @Override
    public SubscriberResponseDTO activateSubscriber(UUID id) {
        Subscriber s = repo.findById(id).orElseThrow(() -> new RuntimeException("Subscriber not found"));
        s.activate();
        repo.save(s);
        return toDto(s);
    }

    @Override
    public SubscriberResponseDTO suspendSubscriber(UUID id) {
        Subscriber s = repo.findById(id).orElseThrow(() -> new RuntimeException("Subscriber not found"));
        s.suspend();
        repo.save(s);
        return toDto(s);
    }

    private SubscriberResponseDTO toDto(Subscriber s) {
        SubscriberResponseDTO dto = new SubscriberResponseDTO();
        dto.setSubscriberId(s.getSubscriberId());
        dto.setFullName(s.getFullName());
        dto.setEmail(s.getEmail());
        dto.setPhoneNumber(s.getPhoneNumber());
        dto.setAddress(s.getAddress());
        dto.setActivationDate(s.getActivationDate());
        dto.setStatus(s.getStatus());
        return dto;
    }
}
