package com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByProductId(Long productId);
    List<Favorite> findByProductId(Long productId);
}
