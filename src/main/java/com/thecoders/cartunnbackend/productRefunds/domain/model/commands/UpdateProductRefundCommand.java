package com.thecoders.cartunnbackend.productRefunds.domain.model.commands;
public record UpdateProductRefundCommand(
        Long id,
        String title,
        String description,
        String status
) {
}
