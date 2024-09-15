package com.thecoders.cartunnbackend.notifications.interfaces.rest.transform;

import com.thecoders.cartunnbackend.notifications.domain.model.commands.UpdateNotificationCommand;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.UpdateNotificationResource;

public class UpdateNotificationCommandFromResourceAssembler {
    public static UpdateNotificationCommand toCommandFromResource(Long orderId, UpdateNotificationResource resource) {
        return new UpdateNotificationCommand(orderId, resource.type(), resource.description());
    }
}