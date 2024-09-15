package com.thecoders.cartunnbackend.profiles.interfaces.rest.transform;


import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile entity){
        return new ProfileResource(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }
}
