package com.telecom.api.service.impl;

import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.exception.NotFoundException;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.SubscriberService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository repo;

    public SubscriberServiceImpl(SubscriberRepository repo) {
        this.repo = repo;
    }

    @Override
    public SubscriberResponseDTO createSubscriber(SubscriberRequestDTO request) {
        log.info("Creating subscriber with phoneNumber: {}", request.getPhoneNumber());

        repo.findByPhoneNumber(request.getPhoneNumber())
                .ifPresent(s -> {
                    log.warn("Duplicate phoneNumber found: {}", request.getPhoneNumber());
                    throw new IllegalArgumentException("Subscriber already exists");
                });

        Subscriber s = Subscriber.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();

        repo.save(s);
        log.info("Subscriber created with ID: {}", s.getSubscriberId());

        return toDto(s);
    }

    @Override
    public SubscriberResponseDTO getSubscriber(UUID id) {
        log.info("Fetching subscriber with ID: {}", id);

        Subscriber s = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Subscriber not found: {}", id);
                    return new NotFoundException("Subscriber not found");
                });

        return toDto(s);
    }

    @Override
    public List<SubscriberResponseDTO> listSubscribers(String status) {
        log.info("Listing subscribers, status filter: {}", status);

        List<Subscriber> list = (status == null || status.isBlank())
                ? repo.findAll()
                : repo.findByStatus(status);

        log.info("Found {} subscribers", list.size());

        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public SubscriberResponseDTO updateSubscriber(UUID id, SubscriberRequestDTO request) {
        log.info("Updating subscriber {} ", id);

        Subscriber s = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Subscriber not found"));

        s.setFullName(request.getFullName());
        s.setEmail(request.getEmail());
        s.setPhoneNumber(request.getPhoneNumber());
        s.setAddress(request.getAddress());
        repo.save(s);

        log.info("Subscriber updated: {}", id);

        return toDto(s);
    }

    @Override
    public void deleteSubscriber(UUID id) {
        log.warn("Deleting subscriber {}", id);

        if (!repo.existsById(id)) {
            log.error("Delete failed, subscriber not found: {}", id);
            throw new NotFoundException("Subscriber not found");
        }

        repo.deleteById(id);
        log.info("Subscriber deleted: {}", id);
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
