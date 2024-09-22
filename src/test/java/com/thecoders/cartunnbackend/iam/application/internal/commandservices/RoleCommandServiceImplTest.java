package com.thecoders.cartunnbackend.iam.application.internal.commandservices;

import com.thecoders.cartunnbackend.iam.domain.model.commands.SeedRolesCommand;
import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;
import com.thecoders.cartunnbackend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleCommandServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleCommandServiceImpl roleCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleSeedRoles_GivenNonExistingRoles_ShouldSaveAllRoles() {
        // Arrange
        SeedRolesCommand command = new SeedRolesCommand();
        when(roleRepository.existsByName(any(Roles.class))).thenReturn(false);

        // Act
        roleCommandService.handle(command);

        // Assert
        Arrays.stream(Roles.values()).forEach(role -> verify(roleRepository).existsByName(role));
    }

    @Test
    void handle_GivenRolesExist_ShouldNotSaveAnyRole() {
        // Arrange
        SeedRolesCommand command = new SeedRolesCommand();
        when(roleRepository.existsByName(any(Roles.class))).thenReturn(true);

        // Act
        roleCommandService.handle(command);

        // Assert
        Arrays.stream(Roles.values()).forEach(role -> {
            verify(roleRepository).existsByName(role);
            verify(roleRepository, never()).save(any(Role.class));
        });
    }
}