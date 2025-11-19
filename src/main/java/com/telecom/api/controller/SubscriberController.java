package com.telecom.api.controller;

import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;
import com.telecom.api.service.SubscriberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {

    private final SubscriberService service;

    public SubscriberController(SubscriberService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponseDTO> create(@RequestBody SubscriberRequestDTO req) {
        SubscriberResponseDTO res = service.createSubscriber(req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.getSubscriber(id));
    }

    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> list(@RequestParam(value = "status", required = false) String status) {
        return ResponseEntity.ok(service.listSubscribers(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriberResponseDTO> update(@PathVariable("id") UUID id, @RequestBody SubscriberRequestDTO req) {
        return ResponseEntity.ok(service.updateSubscriber(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.deleteSubscriber(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<SubscriberResponseDTO> activate(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.activateSubscriber(id));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<SubscriberResponseDTO> suspend(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.suspendSubscriber(id));
    }
}
