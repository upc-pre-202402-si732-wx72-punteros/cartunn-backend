package com.thecoders.cartunnbackend.purchasing.interfaces.rest;


import com.thecoders.cartunnbackend.purchasing.domain.model.commands.DeleteOrderCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllOrdersQuery;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetOrderByIdQuery;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderCommandService;
import com.thecoders.cartunnbackend.purchasing.domain.services.OrderQueryService;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.CreateOrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.UpdateOrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.CreateOrderCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.OrderResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.OrderResource;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform.UpdateOrderCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/orders", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Orders", description = "Notification Management Endpoints")
public class OrdersController {
    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrdersController(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }
    @PostMapping
    public ResponseEntity<OrderResource> createOrder(@RequestBody CreateOrderResource createOrderResource) {
        var createOrderCommand = CreateOrderCommandFromResourceAssembler.toCommandFromResource(createOrderResource);
        var orderId = orderCommandService.handle(createOrderCommand);
        if (orderId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getOrderByIdQuery = new GetOrderByIdQuery(orderId);
        var order = orderQueryService.handle(getOrderByIdQuery);
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return new ResponseEntity<>(orderResource, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResource> getOrder(@PathVariable Long orderId) {
        var getOrderByIdQuery = new GetOrderByIdQuery(orderId);
        var order = orderQueryService.handle(getOrderByIdQuery);
        if (order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return ResponseEntity.ok(orderResource);
    }
    @GetMapping
    public ResponseEntity<List<OrderResource>> getAllOrders() {
        var getAllOrdersQuery = new GetAllOrdersQuery();
        var orders = orderQueryService.handle(getAllOrdersQuery);
        var orderResources = orders.stream().map(OrderResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(orderResources);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResource> updateOrder(@PathVariable Long orderId, @RequestBody UpdateOrderResource updateOrderResource) {
        var updateOrderCommand = UpdateOrderCommandFromResourceAssembler.toCommandFromResource(orderId, updateOrderResource);
        var updatedOrder = orderCommandService.handle(updateOrderCommand);
        if (updatedOrder.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(updatedOrder.get());
        return ResponseEntity.ok(orderResource);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        var deleteOrderCommand = new DeleteOrderCommand(orderId);
        orderCommandService.handle(deleteOrderCommand);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}
