package com.thecoders.cartunnbackend.iam.domain.model.queries;

import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}
