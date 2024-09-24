// Test class for OrderNotificationsController
package com.thecoders.cartunnbackend.purchasing.interfaces.rest;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationQueryService;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.NotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllNotificationsByOrderIdQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderNotificationsControllerTest {

    @Mock
    private NotificationQueryService notificationQueryService;

    @InjectMocks
    private OrderNotificationsController orderNotificationsController;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getAllNotificationsByOrderId() {
        Long orderId = 1L;
        GetAllNotificationsByOrderIdQuery query = new GetAllNotificationsByOrderIdQuery(orderId);
        Order order = new Order(); // Mock Order object
        List<Notification> notifications = List.of(
                new Notification(order, "type1", "description1"),
                new Notification(order, "type2", "description2")
        );
        List<NotificationResource> notificationResources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        when(notificationQueryService.handle(query)).thenReturn(notifications);

        ResponseEntity<List<NotificationResource>> response = orderNotificationsController.getAllNotificationsByOrderId(orderId);

        assertEquals(ResponseEntity.ok(notificationResources), response);
        verify(notificationQueryService, times(1)).handle(query);
    }
}