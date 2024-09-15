package com.thecoders.cartunnbackend.purchasing.domain.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long aLong) {
        super("Notification with id " + aLong + " not found");
    }
}
