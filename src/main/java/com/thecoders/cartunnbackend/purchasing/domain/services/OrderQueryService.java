package com.thecoders.cartunnbackend.purchasing.domain.services;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllOrdersQuery;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetOrderByIdQuery;

import java.util.List;
import java.util.Optional;

public interface OrderQueryService {
    Optional<Order> handle(GetOrderByIdQuery query);

    List<Order> handle(GetAllOrdersQuery query);
}
