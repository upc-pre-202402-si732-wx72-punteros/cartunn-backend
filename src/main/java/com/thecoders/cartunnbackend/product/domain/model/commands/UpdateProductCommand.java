package com.thecoders.cartunnbackend.product.domain.model.commands;

public record UpdateProductCommand(Long id, String title, String description, String image, Double price) {
}
