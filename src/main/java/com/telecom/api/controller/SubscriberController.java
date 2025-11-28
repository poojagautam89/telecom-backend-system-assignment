package com.telecom.api.controller;

import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;
import com.telecom.api.service.SubscriberService;
import com.telecom.api.utils.LoggingUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {

    private final SubscriberService service;

    public SubscriberController(SubscriberService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponseDTO> create(@Valid @RequestBody SubscriberRequestDTO req) {
        // Mask phone number for logs to avoid logging full PII
        String masked = LoggingUtils.maskMsisdn(req.getPhoneNumber());
        log.info("API POST /api/subscribers called - phoneNumber: {}", masked);

        SubscriberResponseDTO res = service.createSubscriber(req);
        log.debug("Create response - id: {}", res.getSubscriberId());

        // Build Location URI: /api/subscribers/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(res.getSubscriberId())
                .toUri();

        return ResponseEntity.created(location).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> getById(@PathVariable("id") UUID id) {
        log.info("API GET /api/subscribers/{} called", id);
        SubscriberResponseDTO res = service.getSubscriber(id);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> list(@RequestParam(value = "status", required = false) String status) {
        log.info("API GET /api/subscribers called with status={}", status);
        List<SubscriberResponseDTO> list = service.listSubscribers(status);
        log.debug("List returned count={}", list == null ? 0 : list.size());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> update(@PathVariable("id") UUID id, @Valid @RequestBody SubscriberRequestDTO req) {
        String masked = LoggingUtils.maskMsisdn(req.getPhoneNumber());
        log.info("API PUT /api/subscribers/{} called - phoneNumber: {}", id, masked);
        SubscriberResponseDTO res = service.updateSubscriber(id, req);
        log.debug("Update completed for id={}", id);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        log.info("API DELETE /api/subscribers/{} called", id);
        service.deleteSubscriber(id);
        log.debug("Delete completed for id={}", id);
        return ResponseEntity.noContent().build();
    }
}
