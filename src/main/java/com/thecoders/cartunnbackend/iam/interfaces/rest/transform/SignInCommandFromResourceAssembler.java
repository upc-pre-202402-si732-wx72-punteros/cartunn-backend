package com.thecoders.cartunnbackend.iam.interfaces.rest.transform;

import com.thecoders.cartunnbackend.iam.domain.model.commands.SignInCommand;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
  public static SignInCommand toCommandFromResource(SignInResource resource) {
    return new SignInCommand(resource.username(), resource.password());
  }
}
