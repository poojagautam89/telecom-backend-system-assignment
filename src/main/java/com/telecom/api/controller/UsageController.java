package com.telecom.api.controller;


import com.telecom.api.dto.UsageRequest;
import com.telecom.api.service.UsageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> ingest(@Valid @RequestBody UsageRequest request) {
        usageService.saveUsage(request);
        return ResponseEntity.status(201).build();
    }
}

