package com.thecoders.cartunnbackend.purchasing.application.internal.queryservices;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllOrdersQuery;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetOrderByIdQuery;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderQueryService;
import com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories.PurchasingOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private final PurchasingOrderRepository orderRepository;

    public OrderQueryServiceImpl(PurchasingOrderRepository orderRepository) {this.orderRepository = orderRepository;}

    @Override
    public Optional<Order> handle(GetOrderByIdQuery query) {return orderRepository.findById(query.orderId());}
    @Override
    public List<Order> handle(GetAllOrdersQuery query) {return orderRepository.findAll();}


}
