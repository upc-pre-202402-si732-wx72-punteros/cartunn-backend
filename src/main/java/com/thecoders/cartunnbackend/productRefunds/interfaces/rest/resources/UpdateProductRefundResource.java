package com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources;

public record UpdateProductRefundResource(
        String title,
        String description,
        String status
) {
}
