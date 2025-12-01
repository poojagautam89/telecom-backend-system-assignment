package com.telecom.api.impl;

import com.telecom.api.dto.UsageRequest;
import com.telecom.api.entity.Sim;
import com.telecom.api.entity.UsageRecord;
import com.telecom.api.enum_pack.UsageType;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.UsageRecordRepository;
import com.telecom.api.service.impl.UsageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsageServiceImplTest {

    @Mock
    private UsageRecordRepository usageRepo;

    @Mock
    private SimRepository simRepo;

    @InjectMocks
    private UsageServiceImpl usageService;

    private UUID simId;
    private Sim sim;

    @BeforeEach
    void setUp() {
        simId = UUID.randomUUID();
        sim = new Sim();
        sim.setSimId(simId);
        sim.setImsi("imsi-test");
    }

    @Test
    void saveUsage_whenSimExists_savesUsageRecord() {
        UsageRequest req = new UsageRequest();
        req.setSimId(simId);
        req.setType("DATA"); // enum mapping
        req.setValue(1024L); // KB
        req.setTimestamp(LocalDateTime.now());

        when(simRepo.findById(simId)).thenReturn(Optional.of(sim));
        when(usageRepo.save(any(UsageRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        usageService.saveUsage(req);

        ArgumentCaptor<UsageRecord> captor = ArgumentCaptor.forClass(UsageRecord.class);
        verify(usageRepo, times(1)).save(captor.capture());
        UsageRecord saved = captor.getValue();

        assertThat(saved.getSim()).isEqualTo(sim);
        assertThat(saved.getUsageType()).isEqualTo(UsageType.DATA);
        assertThat(saved.getUsageValue()).isEqualTo(1024L);
        assertThat(saved.getUsageTimestamp()).isEqualTo(req.getTimestamp());
    }

    @Test
    void saveUsage_whenSimNotFound_throws() {
        UsageRequest req = new UsageRequest();
        req.setSimId(UUID.randomUUID());
        req.setType("CALL");
        req.setValue(10L);
        req.setTimestamp(LocalDateTime.now());

        when(simRepo.findById(req.getSimId())).thenReturn(Optional.empty());

        try {
            usageService.saveUsage(req);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("SIM not found");
        }

        verify(usageRepo, never()).save(any());
    }
}
