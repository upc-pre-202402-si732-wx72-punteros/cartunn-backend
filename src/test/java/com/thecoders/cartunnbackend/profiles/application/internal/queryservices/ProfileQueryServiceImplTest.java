package com.thecoders.cartunnbackend.profiles.application.internal.queryservices;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetAllProfilesQuery;
import com.thecoders.cartunnbackend.profiles.domain.model.queries.GetProfileByIdQuery;
import com.thecoders.cartunnbackend.profiles.infrastructure.jpa.persistence.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProfileQueryServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileQueryServiceImpl profileQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetProfileById_GivenExistingProfile_ShouldReturnProfile() {
        // Arrange
        Long profileId = 1L;
        Profile profile = new Profile("John", "Doe", "john.doe@example.com");
        //profile.setId(profileId);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        GetProfileByIdQuery query = new GetProfileByIdQuery(profileId);

        // Act
        Optional<Profile> result = profileQueryService.handle(query);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(profile);
        verify(profileRepository, times(1)).findById(profileId);
    }

    @Test
    void handleGetProfileById_GivenNonExistingProfile_ShouldReturnEmptyOptional() {
        // Arrange
        Long profileId = 1L;

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        GetProfileByIdQuery query = new GetProfileByIdQuery(profileId);

        // Act
        Optional<Profile> result = profileQueryService.handle(query);

        // Assert
        assertThat(result).isEmpty();
        verify(profileRepository, times(1)).findById(profileId);
    }

    @Test
    void handleGetAllProfiles_GivenExistingProfiles_ShouldReturnListOfProfiles() {
        // Arrange
        Profile profile1 = new Profile("John", "Doe", "john.doe@example.com");
        Profile profile2 = new Profile("Jane", "Smith", "jane.smith@example.com");

        List<Profile> profiles = List.of(profile1, profile2);
        when(profileRepository.findAll()).thenReturn(profiles);

        GetAllProfilesQuery query = new GetAllProfilesQuery();

        // Act
        List<Profile> result = profileQueryService.handle(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactly(profile1, profile2);
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void handleGetAllProfiles_GivenNoProfiles_ShouldReturnEmptyList() {
        // Arrange
        when(profileRepository.findAll()).thenReturn(List.of());

        GetAllProfilesQuery query = new GetAllProfilesQuery();

        // Act
        List<Profile> result = profileQueryService.handle(query);

        // Assert
        assertThat(result).isEmpty();
        verify(profileRepository, times(1)).findAll();
    }
}
