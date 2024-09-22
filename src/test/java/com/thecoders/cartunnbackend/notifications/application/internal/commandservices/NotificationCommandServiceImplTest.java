package com.thecoders.cartunnbackend.notifications.application.internal.commandservices;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.CreateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.DeleteNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.UpdateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.infrastructure.persitence.jpa.repositories.NotificationRepository;
import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories.PurchasingOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationCommandServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PurchasingOrderRepository purchasingOrderRepository;

    @InjectMocks
    private NotificationCommandServiceImpl notificationCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_CreateNotificationCommand_Success() {
        CreateNotificationCommand command = mock(CreateNotificationCommand.class);
        when(command.orderId()).thenReturn(1L);
        when(command.type()).thenReturn("type");
        when(purchasingOrderRepository.findById(1L)).thenReturn(Optional.of(mock(Order.class)));
        when(notificationRepository.existsByType("type")).thenReturn(false);
        Notification notification = mock(Notification.class);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notification.getId()).thenReturn(1L); // Mock getId method

        Long result = notificationCommandService.handle(command);

        assertNotNull(result);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void handle_CreateNotificationCommand_OrderDoesNotExist() {
        CreateNotificationCommand command = mock(CreateNotificationCommand.class);
        when(command.orderId()).thenReturn(1L);
        when(purchasingOrderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationCommandService.handle(command);
        });

        assertEquals("Order does not exist", exception.getMessage());
    }

    @Test
    void handle_UpdateNotificationCommand_Success() {
        UpdateNotificationCommand command = mock(UpdateNotificationCommand.class);
        when(command.id()).thenReturn(1L);
        when(command.type()).thenReturn("type");
        when(command.description()).thenReturn("description");
        when(notificationRepository.existsByTypeAndIdIsNot("type", 1L)).thenReturn(false);
        Notification notification = mock(Notification.class);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification); // Mock save method

        Optional<Notification> result = notificationCommandService.handle(command);

        assertTrue(result.isPresent());
        verify(notification).updateInformation("type", "description");
        verify(notificationRepository).save(notification);
    }

    @Test
    void handle_UpdateNotificationCommand_NotificationDoesNotExist() {
        UpdateNotificationCommand command = mock(UpdateNotificationCommand.class);
        when(command.id()).thenReturn(1L);
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationCommandService.handle(command);
        });

        assertEquals("Notification does not exist", exception.getMessage());
    }

    @Test
    void handle_DeleteNotificationCommand_Success() {
        DeleteNotificationCommand command = mock(DeleteNotificationCommand.class);
        when(command.notificationId()).thenReturn(1L);
        when(notificationRepository.existsById(1L)).thenReturn(true);

        notificationCommandService.handle(command);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    void handle_DeleteNotificationCommand_NotificationDoesNotExist() {
        DeleteNotificationCommand command = mock(DeleteNotificationCommand.class);
        when(command.notificationId()).thenReturn(1L);
        when(notificationRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationCommandService.handle(command);
        });

        assertEquals("Notification does not exist", exception.getMessage());
    }
}