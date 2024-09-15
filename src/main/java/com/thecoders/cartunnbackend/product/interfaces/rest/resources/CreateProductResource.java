package com.thecoders.cartunnbackend.product.interfaces.rest.resources;

public record CreateProductResource(String title, String description, String image, Double price) {
}
