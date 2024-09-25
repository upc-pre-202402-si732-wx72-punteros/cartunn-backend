package com.thecoders.cartunnbackend.purchasing.application.internal.queryservices;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllOrdersQuery;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetOrderByIdQuery;
import com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories.PurchasingOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderQueryServiceImplTest {

    @Mock
    private PurchasingOrderRepository orderRepository;

    @InjectMocks
    private OrderQueryServiceImpl orderQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetOrderByIdQuery_GivenExistingOrder_ShouldReturnOrder() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order("Order1", "Description", 123, null, null, "NEW");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        GetOrderByIdQuery query = new GetOrderByIdQuery(orderId);

        // Act
        Optional<Order> result = orderQueryService.handle(query);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(order);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void handleGetOrderByIdQuery_GivenNonExistingOrder_ShouldReturnEmptyOptional() {
        // Arrange
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        GetOrderByIdQuery query = new GetOrderByIdQuery(orderId);

        // Act
        Optional<Order> result = orderQueryService.handle(query);

        // Assert
        assertThat(result).isEmpty();
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void handleGetAllOrdersQuery_GivenExistingOrders_ShouldReturnListOfOrders() {
        // Arrange
        Order order1 = new Order("Order1", "Description1", 123, null, null, "NEW");
        Order order2 = new Order("Order2", "Description2", 456, null, null, "SHIPPED");

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findAll()).thenReturn(orders);

        GetAllOrdersQuery query = new GetAllOrdersQuery();

        // Act
        List<Order> result = orderQueryService.handle(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactly(order1, order2);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void handleGetAllOrdersQuery_GivenNoOrders_ShouldReturnEmptyList() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of());

        GetAllOrdersQuery query = new GetAllOrdersQuery();

        // Act
        List<Order> result = orderQueryService.handle(query);

        // Assert
        assertThat(result).isEmpty();
        verify(orderRepository, times(1)).findAll();
    }
}
