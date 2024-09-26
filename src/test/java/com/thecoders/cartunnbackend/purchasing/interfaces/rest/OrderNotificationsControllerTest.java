package com.thecoders.cartunnbackend.purchasing.interfaces.rest;

import com.thecoders.cartunnbackend.notifications.domain.services.NotificationQueryService;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.NotificationResource;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllNotificationsByOrderIdQuery;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderNotificationsController.class)
class OrderNotificationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NotificationQueryService notificationQueryService;

    @InjectMocks
    private OrderNotificationsController orderNotificationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllNotificationsByOrderId_GivenValidOrderId_ShouldReturnNotificationList() throws Exception {
        // Arrange
        Long orderId = 1L;

        // Simular NotificationResource en lugar de Notification
        NotificationResource notification1 = new NotificationResource(1L, "Notification 1", "This is notification 1");
        NotificationResource notification2 = new NotificationResource(2L, "Notification 2", "This is notification 2");

        // El servicio debería devolver NotificationResource
        //when(notificationQueryService.handle(any(GetAllNotificationsByOrderIdQuery.class))).thenReturn(List.of(notification1, notification2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders/{orderId}/notifications", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Notification 1"))
                .andExpect(jsonPath("$[0].message").value("This is notification 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Notification 2"))
                .andExpect(jsonPath("$[1].message").value("This is notification 2"));
    }

    @Test
    void getAllNotificationsByOrderId_GivenNoNotifications_ShouldReturnEmptyList() throws Exception {
        // Arrange
        Long orderId = 1L;
        when(notificationQueryService.handle(any(GetAllNotificationsByOrderIdQuery.class)))
                .thenReturn(List.of()); // Lista vacía de NotificationResource

        // Act & Assert
        mockMvc.perform(get("/api/v1/orders/{orderId}/notifications", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
