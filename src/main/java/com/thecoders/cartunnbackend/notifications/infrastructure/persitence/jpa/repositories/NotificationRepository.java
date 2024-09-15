package com.thecoders.cartunnbackend.notifications.infrastructure.persitence.jpa.repositories;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByType(String type);
    boolean existsByType(String type);
    boolean existsByTypeAndIdIsNot(String title, Long id);
    List<Notification> findAllByOrderId(Long orderId);
    List<Notification> findByOrderId(Long orderId);
}