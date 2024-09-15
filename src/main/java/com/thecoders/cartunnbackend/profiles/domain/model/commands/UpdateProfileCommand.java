package com.thecoders.cartunnbackend.profiles.domain.model.commands;

public record UpdateProfileCommand(Long id,String name,String lastName,String email) {
}
