package com.thecoders.cartunnbackend.purchasing.interfaces.rest;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllOrdersQuery;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetOrderByIdQuery;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderCommandService;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderQueryService;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.CreateOrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.OrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.UpdateOrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.OrderResourceFromEntityAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderCommandService orderCommandService;

    @Mock
    private OrderQueryService orderQueryService;

    @InjectMocks
    private OrdersController ordersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_GivenValidRequest_ShouldReturnCreatedOrder() throws Exception {
        // Arrange
        CreateOrderResource createOrderResource = new CreateOrderResource("Order1", "Description", 123, null, null, "NEW");
        Order order = new Order("Order1", "Description", 123, null, null, "NEW");
        //order.setId(1L); // Simulamos el ID generado

        // Simulación de los servicios
        //when(orderCommandService.handle(any())).thenReturn(1L);
        when(orderQueryService.handle(any(GetOrderByIdQuery.class))).thenReturn(Optional.of(order));
        when(OrderResourceFromEntityAssembler.toResourceFromEntity(any(Order.class)))
                .thenReturn(new OrderResource(1L, "Order1", "Description", 123, null, null, "NEW"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Order1\", \"description\": \"Description\", \"code\": 123, \"status\": \"NEW\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Order1"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.code").value(123))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getOrder_GivenExistingOrder_ShouldReturnOrder() throws Exception {
        // Arrange
        Long orderId = 1L;
        GetOrderByIdQuery query = new GetOrderByIdQuery(orderId); // Usamos el tipo correcto
        Order order = new Order("Order1", "Description", 123, null, null, "NEW");
        //order.setId(orderId);

        // Simulamos los servicios
        when(orderQueryService.handle(query)).thenReturn(Optional.of(order));
        when(OrderResourceFromEntityAssembler.toResourceFromEntity(any(Order.class)))
                .thenReturn(new OrderResource(orderId, "Order1", "Description", 123, null, null, "NEW"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Order1"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.code").value(123))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getOrder_GivenNonExistingOrder_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long orderId = 1L;
        GetOrderByIdQuery query = new GetOrderByIdQuery(orderId);

        // Simulamos que no se encuentra el pedido
        when(orderQueryService.handle(query)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        // Arrange
        GetAllOrdersQuery query = new GetAllOrdersQuery();
        Order order1 = new Order("Order1", "Description1", 123, null, null, "NEW");
        Order order2 = new Order("Order2", "Description2", 456, null, null, "SHIPPED");

        // Simulamos la respuesta del servicio
        when(orderQueryService.handle(query)).thenReturn(List.of(order1, order2));
        when(OrderResourceFromEntityAssembler.toResourceFromEntity(any(Order.class)))
                .thenReturn(new OrderResource(1L, "Order1", "Description1", 123, null, null, "NEW"))
                .thenReturn(new OrderResource(2L, "Order2", "Description2", 456, null, null, "SHIPPED"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Order1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Order2"))
                .andExpect(jsonPath("$[1].description").value("Description2"));
    }

    @Test
    void updateOrder_GivenValidRequest_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        Long orderId = 1L;
        UpdateOrderResource updateOrderResource = new UpdateOrderResource("Updated Order", "Updated Description", 123, null, null, "UPDATED");
        Order updatedOrder = new Order("Updated Order", "Updated Description", 123, null, null, "UPDATED");
        //updatedOrder.setId(orderId);

        // Simulamos los servicios
        //when(orderCommandService.handle(any())).thenReturn(Optional.of(updatedOrder));
        when(OrderResourceFromEntityAssembler.toResourceFromEntity(any(Order.class)))
                .thenReturn(new OrderResource(orderId, "Updated Order", "Updated Description", 123, null, null, "UPDATED"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/orders/{orderId}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Updated Order\", \"description\": \"Updated Description\", \"code\": 123, \"status\": \"UPDATED\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Order"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.code").value(123))
                .andExpect(jsonPath("$.status").value("UPDATED"));
    }

    @Test
    void deleteOrder_GivenValidOrderId_ShouldReturnSuccessMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/orders/{orderId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification deleted successfully"));
    }
}
