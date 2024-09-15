package com.thecoders.cartunnbackend.product.domain.model.commands;

public record CreateProductCommand(String title, String description, String image, Double price) {
}
