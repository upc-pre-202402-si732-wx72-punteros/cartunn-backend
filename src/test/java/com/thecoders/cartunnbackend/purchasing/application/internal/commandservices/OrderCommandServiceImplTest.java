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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderCommandServiceImplTest {

    @Mock
    private PurchasingOrderRepository purchasingOrderRepository;

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateOrderCommand_GivenExistingOrderName_ShouldThrowException() {
        // Arrange
        CreateOrderCommand command = new CreateOrderCommand("Order1", "Description", 123, LocalDate.now(), LocalDate.now(), "NEW");

        when(purchasingOrderRepository.existsByName(command.name())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Notification with same order already exists");
    }

    @Test
    void handleCreateOrderCommand_GivenValidCommand_ShouldSaveOrderAndReturnId() {
        // Arrange
        LocalDate entryDate = LocalDate.now();  // Usamos LocalDate en lugar de Date
        LocalDate exitDate = LocalDate.now();   // Usamos LocalDate en lugar de Date
        CreateOrderCommand command = new CreateOrderCommand("Order1", "Description", 123, entryDate, exitDate, "NEW");
        Order order = new Order(command);

        when(purchasingOrderRepository.existsByName(command.name())).thenReturn(false);
        when(purchasingOrderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Long result = orderCommandService.handle(command);

        // Assert
        assertThat(result).isEqualTo(order.getId());
        verify(purchasingOrderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void handleCreateOrderCommand_WhenRepositoryThrowsException_ShouldThrowException() {
        // Arrange
        LocalDate entryDate = LocalDate.now();
        LocalDate exitDate = LocalDate.now();
        CreateOrderCommand command = new CreateOrderCommand("Order1", "Description", 123, entryDate, exitDate, "NEW");

        when(purchasingOrderRepository.existsByName(command.name())).thenReturn(false);
        when(purchasingOrderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while saving order: Database error");
    }

    @Test
    void handleUpdateOrderCommand_GivenExistingOrderName_ShouldThrowException() {
        // Arrange
        UpdateOrderCommand command = new UpdateOrderCommand(1L, "Order1", "Updated Description", 123, LocalDate.now(), LocalDate.now(), "NEW");

        when(purchasingOrderRepository.existsByNameAndIdIsNot(command.name(), command.id())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Notification with same order already exists");
    }

    @Test
    void handleUpdateOrderCommand_GivenNonExistingOrder_ShouldThrowException() {
        // Arrange
        UpdateOrderCommand command = new UpdateOrderCommand(1L, "Order1", "Updated Description", 123, LocalDate.now(), LocalDate.now(), "NEW");

        when(purchasingOrderRepository.findById(command.id())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Notification does not exist");
    }

    @Test
    void handleUpdateOrderCommand_GivenValidCommand_ShouldUpdateAndReturnOrder() {
        // Arrange
        UpdateOrderCommand command = new UpdateOrderCommand(1L, "Order1", "Updated Description", 123, LocalDate.now(), LocalDate.now(), "NEW");
        Order existingOrder = new Order("Order1", "Description", 123, LocalDate.now(), LocalDate.now(), "NEW");

        when(purchasingOrderRepository.existsByNameAndIdIsNot(command.name(), command.id())).thenReturn(false);
        when(purchasingOrderRepository.findById(command.id())).thenReturn(Optional.of(existingOrder));
        when(purchasingOrderRepository.save(any(Order.class))).thenReturn(existingOrder);

        // Act
        Optional<Order> result = orderCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(existingOrder);
        verify(purchasingOrderRepository, times(1)).save(existingOrder);
    }

    @Test
    void handleDeleteOrderCommand_GivenNonExistingOrder_ShouldThrowException() {
        // Arrange
        DeleteOrderCommand command = new DeleteOrderCommand(1L);

        when(purchasingOrderRepository.existsById(command.orderId())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Notification does not exist");
    }

    @Test
    void handleDeleteOrderCommand_GivenExistingOrder_ShouldDeleteOrder() {
        // Arrange
        DeleteOrderCommand command = new DeleteOrderCommand(1L);

        when(purchasingOrderRepository.existsById(command.orderId())).thenReturn(true);

        // Act
        orderCommandService.handle(command);

        // Assert
        verify(purchasingOrderRepository, times(1)).deleteById(command.orderId());
    }

    @Test
    void handleDeleteOrderCommand_WhenRepositoryThrowsException_ShouldThrowException() {
        // Arrange
        DeleteOrderCommand command = new DeleteOrderCommand(1L);

        when(purchasingOrderRepository.existsById(command.orderId())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(purchasingOrderRepository).deleteById(command.orderId());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while deleting order: Database error");
    }
}
