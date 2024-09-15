package com.thecoders.cartunnbackend.product.application.internal.queryservices;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByIdQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteQueryService;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FavoriteQueryServiceImpl implements FavoriteQueryService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteQueryServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Optional<Favorite> handle(GetFavoriteByIdQuery query) {
        return favoriteRepository.findById(query.favoriteId());
    }
    @Override
    public List<Favorite> handle(GetAllFavoritesQuery query) {
        return favoriteRepository.findAll();
    }
    @Override
    public List<Favorite> handle(GetAllFavoritesByProductIdQuery query) {
        return favoriteRepository.findAllByProductId(query.productId());
    }

    @Override
    public List<Favorite> handle(GetFavoriteByProductIdQuery query) {
        return favoriteRepository.findByProductId(query.productId());
    }
}
