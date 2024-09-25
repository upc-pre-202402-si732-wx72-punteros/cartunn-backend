package com.thecoders.cartunnbackend.profiles.application.internal.commandservices;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.CreateProfileCommand;
import com.thecoders.cartunnbackend.profiles.domain.model.commands.UpdateProfileCommand;
import com.thecoders.cartunnbackend.profiles.infrastructure.jpa.persistence.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileCommandServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileCommandServiceImpl profileCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateProfileCommand_GivenExistingEmail_ShouldThrowException() {
        // Arrange
        CreateProfileCommand command = new CreateProfileCommand("John", "Doe", "john.doe@example.com");
        when(profileRepository.existsByEmail(command.email())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Profile with email john.doe@example.com already exists");
    }

    @Test
    void handleCreateProfileCommand_GivenValidCommand_ShouldSaveProfileAndReturnId() {
        // Arrange
        CreateProfileCommand command = new CreateProfileCommand("John", "Doe", "john.doe@example.com");
        Profile profile = new Profile(command);
        //profile.setId(1L);

        when(profileRepository.existsByEmail(command.email())).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        // Act
        Long result = profileCommandService.handle(command);

        // Assert
        assertThat(result).isEqualTo(1L);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void handleCreateProfileCommand_WhenRepositoryThrowsException_ShouldThrowException() {
        // Arrange
        CreateProfileCommand command = new CreateProfileCommand("John", "Doe", "john.doe@example.com");

        when(profileRepository.existsByEmail(command.email())).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while saving profile: Database error");
    }

    @Test
    void handleUpdateProfileCommand_GivenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        UpdateProfileCommand command = new UpdateProfileCommand(1L, "John", "Doe", "john.doe@example.com");
        when(profileRepository.existsByEmailAndIdIsNot(command.email(), command.id())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Profile with same profile already exists");
    }

    @Test
    void handleUpdateProfileCommand_GivenNonExistingProfile_ShouldThrowException() {
        // Arrange
        UpdateProfileCommand command = new UpdateProfileCommand(1L, "John", "Doe", "john.doe@example.com");
        when(profileRepository.findById(command.id())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Profile does not exist");
    }

    @Test
    void handleUpdateProfileCommand_GivenValidCommand_ShouldUpdateAndReturnUpdatedProfile() {
        // Arrange
        UpdateProfileCommand command = new UpdateProfileCommand(1L, "John", "Doe", "john.doe@example.com");
        Profile existingProfile = new Profile("Jane", "Smith", "jane.smith@example.com");
        //existingProfile.setId(1L);

        when(profileRepository.existsByEmailAndIdIsNot(command.email(), command.id())).thenReturn(false);
        when(profileRepository.findById(command.id())).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(existingProfile);

        // Act
        Optional<Profile> result = profileCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(profileRepository, times(1)).save(existingProfile);
    }

    @Test
    void handleUpdateProfileCommand_WhenRepositoryThrowsException_ShouldThrowException() {
        // Arrange
        UpdateProfileCommand command = new UpdateProfileCommand(1L, "John", "Doe", "john.doe@example.com");
        Profile existingProfile = new Profile("Jane", "Smith", "jane.smith@example.com");
        //existingProfile.setId(1L);

        when(profileRepository.existsByEmailAndIdIsNot(command.email(), command.id())).thenReturn(false);
        when(profileRepository.findById(command.id())).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while updating profile: Database error");
    }
}
