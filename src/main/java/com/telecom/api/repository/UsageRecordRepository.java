package com.telecom.api.repository;

import com.telecom.api.entity.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {

    List<UsageRecord> findBySimSimIdAndUsageTimestampBetween(UUID simId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT DATE(u.usageTimestamp) as day, SUM(CASE WHEN u.usageType = com.telecom.api.enum_pack.UsageType.DATA THEN u.usageValue ELSE 0 END) as dataKb, " +
            "SUM(CASE WHEN u.usageType = com.telecom.api.enum_pack.UsageType.CALL THEN u.usageValue ELSE 0 END) as callMin, " +
            "SUM(CASE WHEN u.usageType = com.telecom.api.enum_pack.UsageType.SMS THEN u.usageValue ELSE 0 END) as smsCount " +
            "FROM UsageRecord u WHERE u.sim.simId = :simId AND u.usageTimestamp BETWEEN :start AND :end GROUP BY DATE(u.usageTimestamp) ORDER BY DATE(u.usageTimestamp)")
    List<Object[]> aggregateDailyBySim(@Param("simId") UUID simId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT SUM(CASE WHEN u.usageType = com.telecom.api.enum_pack.UsageType.DATA THEN u.usageValue ELSE 0 END), " +
            "SUM(CASE WHEN u.usageType = com.telecom.api.enum_pack.UsageType.CALL THEN u.usageValue ELSE 0 END), " +
            "SUM(CASE WHEN u.usageType = com.telecom.api.enum_pack.UsageType.SMS THEN u.usageValue ELSE 0 END) " +
            "FROM UsageRecord u WHERE u.sim.simId = :simId AND u.usageTimestamp BETWEEN :start AND :end")
    Object[] aggregateTotalsBySim(@Param("simId") UUID simId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
