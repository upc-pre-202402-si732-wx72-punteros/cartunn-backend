package com.thecoders.cartunnbackend.iam.interfaces.rest.transform;

import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
  public static RoleResource toResourceFromEntity(Role entity) {
    return new RoleResource(entity.getId(), entity.getStringName());

  }
}
