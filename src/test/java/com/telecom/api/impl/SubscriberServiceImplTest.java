package com.telecom.api.impl;

import com.telecom.api.dto.SubscriberRequestDTO;
import com.telecom.api.dto.SubscriberResponseDTO;
import com.telecom.api.entity.Subscriber;
import com.telecom.api.exception.NotFoundException;
import com.telecom.api.repository.SubscriberRepository;
import com.telecom.api.service.impl.SubscriberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository repo;

    @InjectMocks
    private SubscriberServiceImpl service;

    private Subscriber sample;

    @BeforeEach
    void setUp() {
        sample = Subscriber.builder()
                .subscriberId(UUID.randomUUID())
                .fullName("Test User")
                .email("test@example.com")
                .phoneNumber("919812345678")
                .address("Delhi")
                .activationDate(LocalDate.now())
                .status("ACTIVE")
                .build();
    }

    @Test
    void createSubscriber_success() {
        SubscriberRequestDTO req = new SubscriberRequestDTO();
        req.setFullName("New User");
        req.setEmail("new@example.com");
        req.setPhoneNumber("919900112233");
        req.setAddress("Mumbai");

        when(repo.findByPhoneNumber(req.getPhoneNumber())).thenReturn(Optional.empty());
        // repository save should return the entity; service maps it to DTO
        when(repo.save(any(Subscriber.class))).thenAnswer(inv -> inv.getArgument(0));

        SubscriberResponseDTO res = service.createSubscriber(req);

        assertNotNull(res);
        assertEquals(req.getPhoneNumber(), res.getPhoneNumber());
        assertEquals("New User", res.getFullName());
        verify(repo, times(1)).findByPhoneNumber(req.getPhoneNumber());
        verify(repo, times(1)).save(any(Subscriber.class));
    }

    @Test
    void createSubscriber_duplicatePhone_throws() {
        SubscriberRequestDTO req = new SubscriberRequestDTO();
        req.setFullName("Dup User");
        req.setEmail("dup@example.com");
        req.setPhoneNumber(sample.getPhoneNumber());
        req.setAddress("Nowhere");

        when(repo.findByPhoneNumber(sample.getPhoneNumber())).thenReturn(Optional.of(sample));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createSubscriber(req));
        assertTrue(ex.getMessage().toLowerCase().contains("already exists") || ex.getMessage().toLowerCase().contains("exists"));
        verify(repo).findByPhoneNumber(sample.getPhoneNumber());
    }

    @Test
    void getSubscriber_found() {
        UUID id = sample.getSubscriberId();
        when(repo.findById(id)).thenReturn(Optional.of(sample));

        SubscriberResponseDTO dto = service.getSubscriber(id);

        assertNotNull(dto);
        assertEquals(sample.getFullName(), dto.getFullName());
        verify(repo).findById(id);
    }

    @Test
    void getSubscriber_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getSubscriber(id));
        verify(repo).findById(id);
    }

    @Test
    void listSubscribers_withStatus_returnsList() {
        when(repo.findByStatus("ACTIVE")).thenReturn(List.of(sample));

        List<SubscriberResponseDTO> list = service.listSubscribers("ACTIVE");

        assertEquals(1, list.size());
        assertEquals("Test User", list.get(0).getFullName());
        verify(repo).findByStatus("ACTIVE");
    }

    @Test
    void deleteSubscriber_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(repo.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteSubscriber(id));
        verify(repo).existsById(id);
    }
}
