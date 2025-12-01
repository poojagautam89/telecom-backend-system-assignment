package com.telecom.api.repository;

import com.telecom.api.entity.Sim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SimRepository extends JpaRepository<Sim, UUID> {
    List<Sim> findBySubscriberSubscriberId(UUID subscriberId);
}
