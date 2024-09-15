package com.thecoders.cartunnbackend.productRefunds.infrastructure.jpa.persistence;

import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRefundRepository  extends JpaRepository<ProductRefund,Long> {
    Optional<ProductRefund> findByTitle(String tittle);
    boolean existsByTitleAndIdIsNot(String title, Long id);

    boolean existsByTitle(String title);
}
