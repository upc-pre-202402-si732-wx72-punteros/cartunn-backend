package com.thecoders.cartunnbackend.profiles.interfaces.rest.transform;

import com.thecoders.cartunnbackend.profiles.domain.model.commands.CreateProfileCommand;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource){
        return new CreateProfileCommand(
                resource.name(),
                resource.lastName(),
                resource.email()
        );
    }
}
