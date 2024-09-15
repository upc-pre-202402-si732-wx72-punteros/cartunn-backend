package com.thecoders.cartunnbackend.profiles.interfaces.rest.resources;

public record CreateProfileResource(
        String name,
        String lastName,
        String email) { }
