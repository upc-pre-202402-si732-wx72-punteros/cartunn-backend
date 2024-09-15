package com.thecoders.cartunnbackend.product.domain.services;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByIdQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByProductIdQuery;

import java.util.List;
import java.util.Optional;

public interface FavoriteQueryService {

    Optional<Favorite> handle(GetFavoriteByIdQuery query);
    List<Favorite> handle(GetAllFavoritesQuery query);
    List<Favorite> handle(GetAllFavoritesByProductIdQuery query);
    List<Favorite> handle(GetFavoriteByProductIdQuery query);
}

