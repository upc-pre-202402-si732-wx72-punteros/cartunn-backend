package com.thecoders.cartunnbackend.notifications.interfaces.rest.transform;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResourceFromEntity(Notification entity) {
        return new NotificationResource(entity.getId(), entity.getType(), entity.getDescription());
    }
}