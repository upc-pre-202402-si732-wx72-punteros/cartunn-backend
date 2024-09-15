package com.thecoders.cartunnbackend.payment.application.internal.queryservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetAllCartsQuery;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetCartByIdQuery;
import com.thecoders.cartunnbackend.payment.domain.services.CartQueryService;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartQueryServiceImpl implements CartQueryService {
    private final CartRepository cartRepository;

    public CartQueryServiceImpl(CartRepository cartRepository) {this.cartRepository = cartRepository;}

    @Override
    public Optional<Cart> handle(GetCartByIdQuery query) {return cartRepository.findById(query.CartId());}
    @Override
    public List<Cart> handle(GetAllCartsQuery query) {return cartRepository.findAll();}

}
