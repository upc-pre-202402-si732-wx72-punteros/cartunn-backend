package com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PurchasingOrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByName(String title);
    boolean existsByName(String name);
    boolean existsByNameAndIdIsNot(String name, Long id);
}
