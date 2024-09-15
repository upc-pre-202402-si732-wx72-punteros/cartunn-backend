package com.thecoders.cartunnbackend.profiles.domain.services;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetAllProfilesQuery;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetProfileByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    Optional<Profile> handle(GetProfileByIdQuery query);
    List<Profile> handle(GetAllProfilesQuery query);
}
