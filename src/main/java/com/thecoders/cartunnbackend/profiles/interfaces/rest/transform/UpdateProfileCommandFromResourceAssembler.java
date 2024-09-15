package com.thecoders.cartunnbackend.profiles.interfaces.rest.transform;

import com.thecoders.cartunnbackend.profiles.domain.model.commands.UpdateProfileCommand;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource){
        return new UpdateProfileCommand(profileId, resource.name(), resource.lastName(), resource.email());

    }
}
