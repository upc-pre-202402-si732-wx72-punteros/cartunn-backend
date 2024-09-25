package com.thecoders.cartunnbackend.profiles.interfaces.rest;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.CreateProfileCommand;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfilesController.class)
class ProfilesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProfileCommandService profileCommandService;

    @Mock
    private ProfileQueryService profileQueryService;

    @InjectMocks
    private ProfilesController profilesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProfile_GivenValidRequest_ShouldReturnCreatedProfile() throws Exception {
        // Arrange
        CreateProfileResource createProfileResource = new CreateProfileResource("John", "Doe", "john.doe@example.com");
        Profile profile = new Profile("John", "Doe", "john.doe@example.com");

        when(profileCommandService.handle(any(CreateProfileCommand.class))).thenReturn(1L);
        when(profileQueryService.handle(any(GetProfileByIdQuery.class))).thenReturn(Optional.of(profile));
        when(ProfileResourceFromEntityAssembler.toResourceFromEntity(any(Profile.class)))
                .thenReturn(new ProfileResource(1L, "John", "Doe", "john.doe@example.com"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getProfile_GivenExistingProfile_ShouldReturnProfile() throws Exception {
        // Arrange
        Profile profile = new Profile("John", "Doe", "john.doe@example.com");

        when(profileQueryService.handle(any(GetProfileByIdQuery.class))).thenReturn(Optional.of(profile));
        when(ProfileResourceFromEntityAssembler.toResourceFromEntity(any(Profile.class)))
                .thenReturn(new ProfileResource(1L, "John", "Doe", "john.doe@example.com"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/profiles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getProfile_GivenNonExistingProfile_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(profileQueryService.handle(any(GetProfileByIdQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/profiles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProfiles_GivenProfiles_ShouldReturnListOfProfiles() throws Exception {
        // Arrange
        Profile profile1 = new Profile("John", "Doe", "john.doe@example.com");
        Profile profile2 = new Profile("Jane", "Smith", "jane.smith@example.com");

        when(profileQueryService.handle(any(GetAllProfilesQuery.class))).thenReturn(List.of(profile1, profile2));
        when(ProfileResourceFromEntityAssembler.toResourceFromEntity(any(Profile.class)))
                .thenReturn(new ProfileResource(1L, "John", "Doe", "john.doe@example.com"))
                .thenReturn(new ProfileResource(2L, "Jane", "Smith", "jane.smith@example.com"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/profiles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].email").value("jane.smith@example.com"));
    }

    @Test
    void updateProfile_GivenValidRequest_ShouldReturnUpdatedProfile() throws Exception {
        // Arrange
        UpdateProfileResource updateProfileResource = new UpdateProfileResource("John", "Doe", "john.doe@example.com");
        Profile updatedProfile = new Profile("John", "Doe", "john.doe@example.com");

        when(profileCommandService.handle(any(UpdateProfileCommand.class))).thenReturn(Optional.of(updatedProfile));
        when(ProfileResourceFromEntityAssembler.toResourceFromEntity(any(Profile.class)))
                .thenReturn(new ProfileResource(1L, "John", "Doe", "john.doe@example.com"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
}
