package com.thecoders.cartunnbackend.iam.domain.services;

import com.thecoders.cartunnbackend.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
  void handle(SeedRolesCommand command);
}
