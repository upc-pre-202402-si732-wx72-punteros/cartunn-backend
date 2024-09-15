package com.thecoders.cartunnbackend.profiles.domain.services;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.CreateProfileCommand;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;

public interface ProfileCommandService {
    Long handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
}
