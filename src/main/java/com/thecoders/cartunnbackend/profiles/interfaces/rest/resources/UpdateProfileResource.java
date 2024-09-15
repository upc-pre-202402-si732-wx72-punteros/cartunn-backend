package com.thecoders.cartunnbackend.profiles.interfaces.rest.resources;

public record UpdateProfileResource(
        String name,
        String lastName,
        String email
) {
}
