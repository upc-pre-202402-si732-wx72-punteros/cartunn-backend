// Test class for OrderQueryServiceImpl
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderQueryServiceImplTest {

    @Mock
    private PurchasingOrderRepository purchasingOrderRepository;

    @InjectMocks
    private OrderQueryServiceImpl orderQueryService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this);}

    @Test
    void handleGetOrderByIdQuery() {
        GetOrderByIdQuery query = new GetOrderByIdQuery(1L);
        Order order = new Order("order1", "description", 1234, LocalDate.now(),
                LocalDate.now().plusDays(1), "status");
        when(purchasingOrderRepository.findById(query.orderId())).thenReturn(Optional.of(order));

        Optional<Order> result = orderQueryService.handle(query);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        verify(purchasingOrderRepository, times(1)).findById(query.orderId());
    }

    @Test
    void handleGetOrderByIdQuery_NotFound() {
        GetOrderByIdQuery query = new GetOrderByIdQuery(1L);
        when(purchasingOrderRepository.findById(query.orderId())).thenReturn(Optional.empty());

        Optional<Order> result = orderQueryService.handle(query);

        assertFalse(result.isPresent());
        verify(purchasingOrderRepository, times(1)).findById(query.orderId());
    }

    @Test
    void handleGetAllOrdersQuery() {
        GetAllOrdersQuery query = new GetAllOrdersQuery();
        List<Order> orders = List.of(
                new Order("order1", "description", 1234, LocalDate.now(),
                        LocalDate.now().plusDays(1), "status"),
                new Order("order2", "description", 5678, LocalDate.now(),
                        LocalDate.now().plusDays(2), "status")
        );
        when(purchasingOrderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderQueryService.handle(query);

        assertEquals(orders, result);
        verify(purchasingOrderRepository, times(1)).findAll();
    }
}