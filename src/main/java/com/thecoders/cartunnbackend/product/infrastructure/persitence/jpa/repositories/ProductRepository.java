package com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByTitle(String title);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdIsNot(String title, Long id);
}
