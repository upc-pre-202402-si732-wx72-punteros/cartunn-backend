package com.thecoders.cartunnbackend.iam.domain.model.commands;

import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(String username, String password, List<Role> roles) {
}
