package com.thecoders.cartunnbackend.notifications.application.internal.queryservices;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetAllNotificationsQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByOrderIdQuery;
import com.thecoders.cartunnbackend.notifications.infrastructure.persitence.jpa.repositories.NotificationRepository;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllNotificationsByOrderIdQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationQueryServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationQueryServiceImpl notificationQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_GetNotificationByIdQuery_Success() {
        GetNotificationByIdQuery query = mock(GetNotificationByIdQuery.class);
        when(query.notificationId()).thenReturn(1L);
        Notification notification = mock(Notification.class);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationQueryService.handle(query);

        assertTrue(result.isPresent());
        assertEquals(notification, result.get());
    }

    @Test
    void handle_GetNotificationByIdQuery_NotFound() {
        GetNotificationByIdQuery query = mock(GetNotificationByIdQuery.class);
        when(query.notificationId()).thenReturn(1L);
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Notification> result = notificationQueryService.handle(query);

        assertFalse(result.isPresent());
    }

    @Test
    void handle_GetAllNotificationsQuery_Success() {
        GetAllNotificationsQuery query = mock(GetAllNotificationsQuery.class);
        List<Notification> notifications = List.of(mock(Notification.class), mock(Notification.class));
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationQueryService.handle(query);

        assertEquals(notifications, result);
    }

    @Test
    void handle_GetAllNotificationsByOrderIdQuery_Success() {
        GetAllNotificationsByOrderIdQuery query = mock(GetAllNotificationsByOrderIdQuery.class);
        when(query.orderId()).thenReturn(1L);
        List<Notification> notifications = List.of(mock(Notification.class), mock(Notification.class));
        when(notificationRepository.findAllByOrderId(1L)).thenReturn(notifications);

        List<Notification> result = notificationQueryService.handle(query);

        assertEquals(notifications, result);
    }

    @Test
    void handle_GetNotificationByOrderIdQuery_Success() {
        GetNotificationByOrderIdQuery query = mock(GetNotificationByOrderIdQuery.class);
        when(query.orderId()).thenReturn(1L);
        List<Notification> notifications = List.of(mock(Notification.class), mock(Notification.class));
        when(notificationRepository.findByOrderId(1L)).thenReturn(notifications);

        List<Notification> result = notificationQueryService.handle(query);

        assertEquals(notifications, result);
    }
}