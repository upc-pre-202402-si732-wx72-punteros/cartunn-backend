// Test class for OrdersController
package com.thecoders.cartunnbackend.purchasing.interfaces.rest;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.DeleteOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.CreateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.commands.UpdateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllOrdersQuery;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetOrderByIdQuery;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderCommandService;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderQueryService;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.CreateOrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.OrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.UpdateOrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.CreateOrderCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.OrderResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.UpdateOrderCommandFromResourceAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersControllerTest {

    @Mock
    private OrderCommandService orderCommandService;

    @Mock
    private OrderQueryService orderQueryService;

    @InjectMocks
    private OrdersController ordersController;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void createOrder() {
        CreateOrderResource createOrderResource = new CreateOrderResource(
                "Order Name", "Order Description", 1234, LocalDate.now(), LocalDate.now()
                .plusDays(1), "Pending"
        );
        CreateOrderCommand createOrderCommand = CreateOrderCommandFromResourceAssembler
                .toCommandFromResource(createOrderResource);
        Order order = new Order(
                "Order Name", "Order Description", 1234, LocalDate.now(), LocalDate.now()
                .plusDays(1), "Pending"
        );
        OrderResource orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order);

        when(orderCommandService.handle(createOrderCommand)).thenReturn(1L);
        when(orderQueryService.handle(any(GetOrderByIdQuery.class))).thenReturn(Optional.of(order));

        ResponseEntity<OrderResource> response = ordersController.createOrder(createOrderResource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderResource, response.getBody());
        verify(orderCommandService, times(1)).handle(createOrderCommand);
        verify(orderQueryService, times(1)).handle(any(GetOrderByIdQuery.class));
    }

    @Test
    void getOrder() {
        Long orderId = 1L;
        Order order = new Order(
                "Order Name", "Order Description", 1234, LocalDate.now(), LocalDate.now()
                .plusDays(1), "Pending"
        );
        OrderResource orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order);

        when(orderQueryService.handle(any(GetOrderByIdQuery.class))).thenReturn(Optional.of(order));

        ResponseEntity<OrderResource> response = ordersController.getOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResource, response.getBody());
        verify(orderQueryService, times(1)).handle(any(GetOrderByIdQuery.class));
    }

    @Test
    void getAllOrders() {
        List<Order> orders = List.of(
                new Order("Order Name 1", "Order Description 1", 1234, LocalDate.now(),
                        LocalDate.now().plusDays(1), "Pending"),
                new Order("Order Name 2", "Order Description 2", 5678, LocalDate.now(),
                        LocalDate.now().plusDays(2), "Completed")
        );
        List<OrderResource> orderResources = orders.stream()
                .map(OrderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        when(orderQueryService.handle(any(GetAllOrdersQuery.class))).thenReturn(orders);

        ResponseEntity<List<OrderResource>> response = ordersController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResources, response.getBody());
        verify(orderQueryService, times(1)).handle(any(GetAllOrdersQuery.class));
    }

    @Test
    void updateOrder() {
        Long orderId = 1L;
        UpdateOrderResource updateOrderResource = new UpdateOrderResource(
                "Updated Order Name", "Updated Order Description", 1234, LocalDate.now(),
                LocalDate.now().plusDays(1), "Pending"
        );
        UpdateOrderCommand updateOrderCommand = UpdateOrderCommandFromResourceAssembler.toCommandFromResource(orderId,
                updateOrderResource);
        Order order = new Order(
                "Updated Order Name", "Updated Order Description", 1234, LocalDate.now(),
                LocalDate.now().plusDays(1), "Pending"
        );
        OrderResource orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order);

        when(orderCommandService.handle(updateOrderCommand)).thenReturn(Optional.of(order));

        ResponseEntity<OrderResource> response = ordersController.updateOrder(orderId, updateOrderResource);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResource, response.getBody());
        verify(orderCommandService, times(1)).handle(updateOrderCommand);
    }

    @Test
    void deleteOrder() {
        Long orderId = 1L;
        DeleteOrderCommand deleteOrderCommand = new DeleteOrderCommand(orderId);

        doNothing().when(orderCommandService).handle(deleteOrderCommand);

        ResponseEntity<?> response = ordersController.deleteOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notification deleted successfully", response.getBody());
        verify(orderCommandService, times(1)).handle(deleteOrderCommand);
    }
}