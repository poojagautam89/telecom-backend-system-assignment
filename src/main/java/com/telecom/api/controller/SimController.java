package com.telecom.api.controller;

import com.telecom.api.dto.ActivateSimRequest;
import com.telecom.api.dto.AssignPlanRequest;
import com.telecom.api.dto.SimResponseDTO;
import com.telecom.api.service.SimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sim")
public class SimController {

    private final SimService simService;

    public SimController(SimService simService) {
        this.simService = simService;
    }

    @PostMapping("/activate")
    public ResponseEntity<SimResponseDTO> activate(@Valid @RequestBody ActivateSimRequest req) {
        SimResponseDTO dto = simService.activate(req);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> block(@PathVariable("id") UUID id, @RequestParam boolean block) {
        simService.block(id, block);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign-plan")
    public ResponseEntity<Void> assignPlan(@PathVariable("id") UUID id, @Valid @RequestBody AssignPlanRequest req) {
        simService.assignPlan(id, req.getPlanId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscriber/{subscriberId}")
    public ResponseEntity<List<SimResponseDTO>> getBySubscriber(@PathVariable("subscriberId") UUID subscriberId) {
        List<SimResponseDTO> list = simService.getBySubscriber(subscriberId);
        return ResponseEntity.ok(list);
    }
}
