package com.thecoders.cartunnbackend.payment.domain.services;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetAllCartsQuery;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetCartByIdQuery;

import java.util.List;
import java.util.Optional;

public interface CartQueryService {
    Optional<Cart> handle(GetCartByIdQuery query);
    List<Cart> handle(GetAllCartsQuery query);
}
