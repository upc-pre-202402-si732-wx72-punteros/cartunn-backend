package com.thecoders.cartunnbackend.notifications.application.internal.commandservices;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.CreateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.DeleteNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.UpdateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationCommandService;
import com.thecoders.cartunnbackend.notifications.infrastructure.persitence.jpa.repositories.NotificationRepository;
import com.thecoders.cartunnbackend.product.domain.model.commands.RequestFavoriteCommand;
import com.thecoders.cartunnbackend.purchasing.infrastructure.persitence.jpa.repositories.PurchasingOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final PurchasingOrderRepository purchasingOrderRepository;
    public NotificationCommandServiceImpl(NotificationRepository notificationRepository, PurchasingOrderRepository purchasingOrderRepository) {
        this.notificationRepository = notificationRepository;
        this.purchasingOrderRepository = purchasingOrderRepository;
    }

    @Override
    public Long handle(CreateNotificationCommand command) {
        var order = purchasingOrderRepository.findById(command.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order does not exist"));
        if (notificationRepository.existsByType(command.type())) {
            throw new IllegalArgumentException("Notification with same type already exists");
        }
        var notification = new Notification(command, order);
        try {
            notificationRepository.save(notification);
            return notification.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving notification: " + e.getMessage());
        }
    }


    @Override
    public Optional<Notification> handle(UpdateNotificationCommand command) {
        if (notificationRepository.existsByTypeAndIdIsNot(command.type(), command.id())) {
            throw new IllegalArgumentException("Notification with same notification already exists");
        }
        var result = notificationRepository.findById(command.id());
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Notification does not exist");
        }
        var notificationToUpdate = result.get();
        try {
            notificationToUpdate.updateInformation(command.type(), command.description());
            var updatedNotification = notificationRepository.save(notificationToUpdate);
            return Optional.of(updatedNotification);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating notification: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteNotificationCommand command) {
        if (!notificationRepository.existsById(command.notificationId())) {
            throw new IllegalArgumentException("Notification does not exist");
        }
        try {
            notificationRepository.deleteById(command.notificationId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting order: " + e.getMessage());
        }
    }
}
