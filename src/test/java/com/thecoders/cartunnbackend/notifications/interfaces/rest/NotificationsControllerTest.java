package com.thecoders.cartunnbackend.notifications.interfaces.rest;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.CreateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.DeleteNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.UpdateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetAllNotificationsQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByOrderIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationCommandService;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationQueryService;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.CreateNotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.NotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.UpdateNotificationResource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationsControllerTest {

    @Mock
    private NotificationCommandService notificationCommandService;

    @Mock
    private NotificationQueryService notificationQueryService;

    @InjectMocks
    private NotificationsController notificationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNotification_Success() {
        CreateNotificationResource resource = mock(CreateNotificationResource.class);
        when(resource.orderId()).thenReturn(1L);
        when(notificationCommandService.handle(any(CreateNotificationCommand.class))).thenReturn(1L);
        when(notificationQueryService.handle(any(GetNotificationByOrderIdQuery.class)))
                .thenReturn(List.of(mock(Notification.class)));

        ResponseEntity<NotificationResource> response = notificationsController.createNotification(resource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createNotification_Failure() {
        CreateNotificationResource resource = mock(CreateNotificationResource.class);
        when(resource.orderId()).thenReturn(1L);
        when(notificationCommandService.handle(any(CreateNotificationCommand.class))).thenReturn(1L);
        when(notificationQueryService.handle(any(GetNotificationByOrderIdQuery.class)))
                .thenReturn(List.of());

        ResponseEntity<NotificationResource> response = notificationsController.createNotification(resource);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getNotification_Success() {
        when(notificationQueryService.handle(any(GetNotificationByIdQuery.class)))
                .thenReturn(Optional.of(mock(Notification.class)));

        ResponseEntity<NotificationResource> response = notificationsController.getNotification(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getNotification_NotFound() {
        when(notificationQueryService.handle(any(GetNotificationByIdQuery.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<NotificationResource> response = notificationsController.getNotification(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllNotifications_Success() {
        when(notificationQueryService.handle(any(GetAllNotificationsQuery.class)))
                .thenReturn(List.of(mock(Notification.class), mock(Notification.class)));

        ResponseEntity<List<NotificationResource>> response = notificationsController.getAllNotifications();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateNotification_Success() {
        UpdateNotificationResource resource = mock(UpdateNotificationResource.class);
        when(notificationCommandService.handle(any(UpdateNotificationCommand.class))).thenReturn(Optional.of(mock(Notification.class)));

        ResponseEntity<NotificationResource> response = notificationsController.updateNotification(1L, resource);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateNotification_Failure() {
        UpdateNotificationResource resource = mock(UpdateNotificationResource.class);
        when(notificationCommandService.handle(any(UpdateNotificationCommand.class))).thenReturn(Optional.empty());

        ResponseEntity<NotificationResource> response = notificationsController.updateNotification(1L, resource);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteNotification_Success() {
        doNothing().when(notificationCommandService).handle(any(DeleteNotificationCommand.class));

        ResponseEntity<?> response = notificationsController.deleteNotification(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notification deleted successfully", response.getBody());
    }
}