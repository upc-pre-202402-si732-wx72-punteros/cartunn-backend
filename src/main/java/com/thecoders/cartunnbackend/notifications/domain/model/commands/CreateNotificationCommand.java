package com.thecoders.cartunnbackend.notifications.domain.model.commands;

public record CreateNotificationCommand(Long orderId,String type, String description) {
}