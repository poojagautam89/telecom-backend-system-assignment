package com.telecom.api.repository;

import com.telecom.api.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    List<Subscriber> findByStatus(String status);
}
