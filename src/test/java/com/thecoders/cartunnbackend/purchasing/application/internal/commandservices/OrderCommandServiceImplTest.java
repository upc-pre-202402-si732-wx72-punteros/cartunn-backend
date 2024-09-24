// Update the test cases to match the constructors of CreateOrderCommand and UpdateOrderCommand
package com.thecoders.cartunnbackend.purchasing.application.internal.commandservices;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.CreateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.DeleteOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.UpdateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories.PurchasingOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderCommandServiceImplTest {

    @Mock
    private PurchasingOrderRepository purchasingOrderRepository;

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    @BeforeEach
    void setUp() {MockitoAnnotations.openMocks(this);}

    @Test
    void handleCreateOrderCommand() {
        CreateOrderCommand command = new CreateOrderCommand("order1", "description", 1234, LocalDate.now(), LocalDate.now().plusDays(1), "status");
        Order savedOrder = new Order(command);
        savedOrder.setId(1L); // Set the ID to simulate the saved order

        when(purchasingOrderRepository.existsByName(command.name())).thenReturn(false);
        when(purchasingOrderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Long orderId = orderCommandService.handle(command);

        assertNotNull(orderId);
        verify(purchasingOrderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void handleCreateOrderCommand_ThrowsException() {
        CreateOrderCommand command = new CreateOrderCommand("order1", "description", 1234, LocalDate.now(), LocalDate.now().plusDays(1), "status");
        when(purchasingOrderRepository.existsByName(command.name())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderCommandService.handle(command));


        assertEquals("Notification with same order already exists", exception.getMessage());
    }

    @Test
    void handleUpdateOrderCommand() {
        UpdateOrderCommand command = new UpdateOrderCommand(1L, "order1", "description", 1234, LocalDate.now(), LocalDate.now().plusDays(1), "status");
        Order order = new Order("order1", "description", 1234, LocalDate.now(), LocalDate.now().plusDays(1), "status");
        when(purchasingOrderRepository.existsByNameAndIdIsNot(command.name(), command.id())).thenReturn(false);
        when(purchasingOrderRepository.findById(command.id())).thenReturn(Optional.of(order));
        when(purchasingOrderRepository.save(any(Order.class))).thenReturn(order);

        Optional<Order> updatedOrder = orderCommandService.handle(command);

        assertTrue(updatedOrder.isPresent());
        verify(purchasingOrderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void handleUpdateOrderCommand_ThrowsException() {
        UpdateOrderCommand command = new UpdateOrderCommand(1L, "order1", "description", 1234, LocalDate.now(), LocalDate.now().plusDays(1), "status");
        when(purchasingOrderRepository.existsByNameAndIdIsNot(command.name(), command.id())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderCommandService.handle(command));


        assertEquals("Notification with same order already exists", exception.getMessage());
    }

    @Test
    void handleDeleteOrderCommand() {
        DeleteOrderCommand command = new DeleteOrderCommand(1L);
        when(purchasingOrderRepository.existsById(command.orderId())).thenReturn(true);

        orderCommandService.handle(command);

        verify(purchasingOrderRepository, times(1)).deleteById(command.orderId());
    }

    @Test
    void handleDeleteOrderCommand_ThrowsException() {
        DeleteOrderCommand command = new DeleteOrderCommand(1L);
        when(purchasingOrderRepository.existsById(command.orderId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderCommandService.handle(command));


        assertEquals("Notification does not exist", exception.getMessage());
    }
}