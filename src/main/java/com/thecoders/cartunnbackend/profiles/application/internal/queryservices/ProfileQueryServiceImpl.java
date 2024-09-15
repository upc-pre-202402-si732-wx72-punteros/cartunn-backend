package com.thecoders.cartunnbackend.profiles.application.internal.queryservices;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetAllProfilesQuery;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetProfileByIdQuery;
import com.thecoders.cartunnbackend.profiles.domain.services.ProfileQueryService;
import com.thecoders.cartunnbackend.profiles.infrastructure.jpa.persistence.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.profileId());
    }


    @Override
    public List<Profile> handle(GetAllProfilesQuery query){
        return profileRepository.findAll();
    }



}
