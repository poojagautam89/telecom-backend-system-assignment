package com.telecom.api.repository;

import com.telecom.api.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Optional<Subscriber> findByPhoneNumber(String phoneNumber);
    List<Subscriber> findByStatus(String status);
}
