package com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources;

public record ProductRefundResource(
        Long id,
        String title,
        String description,
        String status
) {
}
