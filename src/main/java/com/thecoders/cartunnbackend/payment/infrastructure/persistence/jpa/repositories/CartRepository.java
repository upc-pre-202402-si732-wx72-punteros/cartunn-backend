package com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories;
import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByTotal(BigDecimal total);
    boolean existsById(Long id);
    boolean existsByTotalAndIdIsNot(BigDecimal total, Long id);
}
