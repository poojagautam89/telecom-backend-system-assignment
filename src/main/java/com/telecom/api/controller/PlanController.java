package com.telecom.api.controller;

import com.telecom.api.entity.Plan;
import com.telecom.api.dto.PlanRequestDTO;
import com.telecom.api.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<Plan> create(@Valid @RequestBody PlanRequestDTO req) {
        Plan p = new Plan();
        // map fields
        p.setName(req.getName());
        p.setType(req.getType());
        p.setMonthlyPrice(req.getMonthlyPrice());
        p.setDataAllowanceMb(req.getDataAllowanceMb());
        p.setCallAllowanceMin(req.getCallAllowanceMin());
        p.setSmsAllowance(req.getSmsAllowance());
        Plan saved = planService.create(p);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Plan>> all() {
        return ResponseEntity.ok(planService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> get(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(planService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> update(@PathVariable("id") UUID id, @Valid @RequestBody PlanRequestDTO req) {
        Plan p = new Plan();
        p.setName(req.getName());
        p.setType(req.getType());
        p.setMonthlyPrice(req.getMonthlyPrice());
        p.setDataAllowanceMb(req.getDataAllowanceMb());
        p.setCallAllowanceMin(req.getCallAllowanceMin());
        p.setSmsAllowance(req.getSmsAllowance());
        return ResponseEntity.ok(planService.update(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
