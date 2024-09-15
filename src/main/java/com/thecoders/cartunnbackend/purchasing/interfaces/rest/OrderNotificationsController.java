package com.thecoders.cartunnbackend.purchasing.interfaces.rest;

import com.thecoders.cartunnbackend.notifications.domain.services.NotificationQueryService;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.NotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllNotificationsByOrderIdQuery;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/orders/{orderId}/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications")
public class OrderNotificationsController {
    private final NotificationQueryService notificationQueryService;
    public OrderNotificationsController(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }
    @GetMapping
    public ResponseEntity<List<NotificationResource>> getAllNotificationsByOrderId(@PathVariable Long orderId) {
        var getAllNotificationsByOrderIdQuery = new GetAllNotificationsByOrderIdQuery(orderId);
        var notifications = notificationQueryService.handle(getAllNotificationsByOrderIdQuery);
        var notificationResources = notifications.stream().map(NotificationResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(notificationResources);
    }
}
