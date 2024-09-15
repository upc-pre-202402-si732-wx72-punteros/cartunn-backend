package com.thecoders.cartunnbackend.profiles.application.internal.commandservices;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.CreateProfileCommand;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.UpdateProfileCommand;
import com.thecoders.cartunnbackend.profiles.domain.services.ProfileCommandService;
import com.thecoders.cartunnbackend.profiles.infrastructure.jpa.persistence.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    @Override
    public Long  handle(CreateProfileCommand command){
        if(profileRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Profile with email " + command.email() + "already exists");
        }
        var profile = new Profile(command);
        try {
            profileRepository.save(profile);
            return profile.getId();
        } catch (Exception e){
            throw new IllegalArgumentException("Error while saving profile: " + e.getMessage());

        }

    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command){
        if(profileRepository.existsByEmailAndIdIsNot(command.email(), command.id())){
            throw new IllegalArgumentException("Profile with same profile already exists");
        }
        var result = profileRepository.findById(command.id());
        if (result.isEmpty()){
            throw new IllegalArgumentException("Profile does not exist");
        }
        var profileToUpdated = result.get();
        try {
            profileToUpdated.updateInformation(command.name(), command.lastName(), command.email());
            var updatedProfile = profileRepository.save(profileToUpdated);
            return Optional.of(updatedProfile);
        } catch (Exception e){
            throw new IllegalArgumentException("Error while updating profile: " + e.getMessage());
        }
    }

}

