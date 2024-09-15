package com.thecoders.cartunnbackend.profiles.interfaces.rest.resources;

public record ProfileResource(
        Long id,
        String name,
        String lastName,
        String email
) {
}
