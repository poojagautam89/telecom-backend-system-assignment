package com.telecom.api.repository;

import com.telecom.api.entity.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
    List<UsageRecord> findBySimSimIdAndUsageTimestampBetween(UUID simId, LocalDateTime start, LocalDateTime end);
}
