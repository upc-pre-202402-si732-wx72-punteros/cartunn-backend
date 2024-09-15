package com.thecoders.cartunnbackend.iam.interfaces.rest.transform;

import com.thecoders.cartunnbackend.iam.domain.model.aggregates.User;
import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
  public static UserResource toResourceFromEntity(User entity) {
    var roles = entity.getRoles().stream().map(Role::getStringName).toList();
    return new UserResource(entity.getId(), entity.getUsername(), roles);
  }
}
