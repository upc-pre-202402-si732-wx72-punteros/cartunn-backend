package com.thecoders.cartunnbackend.purchasing.domain.services;
import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.CreateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.DeleteOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.UpdateOrderCommand;

import java.util.Optional;

public interface OrderCommandService {
    Long handle(CreateOrderCommand command);
    Optional<Order> handle(UpdateOrderCommand command);

    void handle(DeleteOrderCommand command);
}
