package com.thecoders.cartunnbackend.iam.interfaces.rest.transform;

import com.thecoders.cartunnbackend.iam.domain.model.aggregates.User;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
  public static AuthenticatedUserResource toResourceFromEntity(User entity, String token) {
    return new AuthenticatedUserResource(entity.getId(), entity.getUsername(), token);
  }
}
