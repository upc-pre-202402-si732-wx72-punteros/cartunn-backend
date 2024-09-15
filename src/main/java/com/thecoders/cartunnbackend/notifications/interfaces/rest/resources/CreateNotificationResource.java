package com.thecoders.cartunnbackend.notifications.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record CreateNotificationResource(
        @NotNull Long orderId,
        String type, String description) {
}