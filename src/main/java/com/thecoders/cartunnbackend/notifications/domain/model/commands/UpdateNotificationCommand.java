package com.thecoders.cartunnbackend.notifications.domain.model.commands;

public record UpdateNotificationCommand(Long id, String type, String description) {
}
