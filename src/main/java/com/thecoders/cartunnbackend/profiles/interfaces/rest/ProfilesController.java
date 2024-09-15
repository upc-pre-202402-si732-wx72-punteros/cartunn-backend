package com.thecoders.cartunnbackend.profiles.interfaces.rest;

import com.thecoders.cartunnbackend.profiles.domain.model.commands.UpdateProfileCommand;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetAllProfilesQuery;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetProfileByIdQuery;
import com.thecoders.cartunnbackend.profiles.domain.services.ProfileCommandService;
import com.thecoders.cartunnbackend.profiles.domain.services.ProfileQueryService;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.resources.CreateProfileResource;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.resources.ProfileResource;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.profiles.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")

public class ProfilesController {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileResource createProfileResource) {
        var createProfileCommand = CreateProfileCommandFromResourceAssembler.toCommandFromResource(createProfileResource);
        var profileId = profileCommandService.handle(createProfileCommand);
        if (profileId == 0L){
            return ResponseEntity.badRequest().build();
        }
        var getProfileByIdQuery = new GetProfileByIdQuery(profileId);
        var profile = profileQueryService.handle(getProfileByIdQuery);
        if(profile.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileResource, HttpStatus.CREATED);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResource> getProfile(@PathVariable Long profileId){
        var getProfileByIdQuery = new GetProfileByIdQuery(profileId);
        var profile = profileQueryService.handle(getProfileByIdQuery);
        if(profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAllProfiles(){
        var getAllProfilesQuery = new GetAllProfilesQuery();
        var profiles = profileQueryService.handle(getAllProfilesQuery);
        var profilesResources = profiles.stream().map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(profilesResources);
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ProfileResource> updateProfile(@PathVariable Long profileId, @RequestBody UpdateProfileResource updateProfileResource){
        var updateProfileCommand = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(profileId, updateProfileResource);
        var updateProfile = profileCommandService.handle(updateProfileCommand);
        if (updateProfile.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updateProfile.get());
        return ResponseEntity.ok(profileResource);
    }

}
