package com.thecoders.cartunnbackend.tunning.domain.exceptions;

public class TunningTaskNotFoundException extends RuntimeException {
    public TunningTaskNotFoundException(Long aLong) {
        super("TunningTask with id " + aLong + " not found");
    }
}
