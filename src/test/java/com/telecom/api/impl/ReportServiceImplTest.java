package com.telecom.api.impl;

import com.telecom.api.dto.UsageSummaryDTO;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.repository.SimRepository;
import com.telecom.api.repository.UsageRecordRepository;
import com.telecom.api.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock private UsageRecordRepository usageRepo;
    @Mock private SimRepository simRepo;
    @Mock private SubscriberRepository subscriberRepo;
    @InjectMocks private ReportServiceImpl reportService;

    @Test
    void getMonthlyUsage_empty_returnsZeros() {
        UUID simId = UUID.randomUUID();
        YearMonth ym = YearMonth.now();
        when(usageRepo.aggregateTotalsBySim(eq(simId), any(), any())).thenReturn(new Object[]{0L, 0L, 0L});
        var dto = reportService.getMonthlyUsage(simId, ym);
        assertThat(dto.getTotalDataKb()).isEqualTo(0);
        assertThat(dto.getTotalCallMin()).isEqualTo(0);
        assertThat(dto.getTotalSms()).isEqualTo(0);
    }
}
