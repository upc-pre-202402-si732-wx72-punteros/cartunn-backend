package com.thecoders.cartunnbackend.notifications.domain.services;
import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.CreateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.DeleteNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.commands.UpdateNotificationCommand;

import java.util.Optional;

public interface NotificationCommandService {
    Long handle(CreateNotificationCommand command);
    Optional<Notification> handle(UpdateNotificationCommand command);
    void handle(DeleteNotificationCommand command);
}
