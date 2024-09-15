package com.thecoders.cartunnbackend.purchasing.application.internal.commandservices;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.CreateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.DeleteOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.UpdateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderCommandService;
import com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories.PurchasingOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {
    private final PurchasingOrderRepository purchasingOrderRepository;

    public OrderCommandServiceImpl(PurchasingOrderRepository orderRepository) {
        this.purchasingOrderRepository = orderRepository;
    }

    @Override
    public Long handle(CreateOrderCommand command) {
        if (purchasingOrderRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Notification with same order already exists");
        }
        var order = new Order(command);
        try {
            purchasingOrderRepository.save(order);
            return order.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving order: " + e.getMessage());
        }
    }

    @Override
    public Optional<Order> handle(UpdateOrderCommand command) {
        if (purchasingOrderRepository.existsByNameAndIdIsNot(command.name(), command.id())) {
            throw new IllegalArgumentException("Notification with same order already exists");
        }
        var result = purchasingOrderRepository.findById(command.id());
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Notification does not exist");
        }
        var orderToUpdate = result.get();
        try {
            orderToUpdate.updateInformation(command.name(), command.description(), command.code(), command.entryDate(), command.exitDate(), command.status());
            var updatedOrder = purchasingOrderRepository.save(orderToUpdate);
            return Optional.of(updatedOrder);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating order: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteOrderCommand command) {
        if (!purchasingOrderRepository.existsById(command.orderId())) {
            throw new IllegalArgumentException("Notification does not exist");
        }
        try {
            purchasingOrderRepository.deleteById(command.orderId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting order: " + e.getMessage());
        }
    }
}
