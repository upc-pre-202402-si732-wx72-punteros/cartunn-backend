package com.thecoders.cartunnbackend.product.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long aLong) {
        super("Product with id " + aLong + " not found");
    }
}
