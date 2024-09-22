package com.thecoders.cartunnbackend.iam.application.internal.commandservices;

import com.thecoders.cartunnbackend.iam.application.internal.outboundservices.hashing.HashingService;
import com.thecoders.cartunnbackend.iam.application.internal.outboundservices.tokens.TokenService;
import com.thecoders.cartunnbackend.iam.domain.model.aggregates.User;
import com.thecoders.cartunnbackend.iam.domain.model.commands.SignInCommand;
import com.thecoders.cartunnbackend.iam.domain.model.commands.SignUpCommand;
import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;
import com.thecoders.cartunnbackend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.thecoders.cartunnbackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleSignUp_GivenCorrectCommand_ShouldReturnsUserRegistered() {
        // Arrange
        Role role1 = new Role(Roles.ROLE_CLIENT);
        Role role2 = new Role(Roles.ROLE_STAFF);
        SignUpCommand command = new SignUpCommand("user1", "password", List.of(role1, role2));

        when(userRepository.existsByUsername(command.username())).thenReturn(false);

        when(roleRepository.findByName(Roles.ROLE_CLIENT)).thenReturn(Optional.of(role1));
        when(roleRepository.findByName(Roles.ROLE_STAFF)).thenReturn(Optional.of(role2));

        String hashedPassword = "hashedPassword";
        when(hashingService.encode(command.password())).thenReturn(hashedPassword);

        User savedUser = new User(command.username(), hashedPassword, List.of(role1, role2));
        when(userRepository.save(savedUser)).thenReturn(savedUser);

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(savedUser));

        // Act
        Optional<User> result = userCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    void handleSignUp_GivenExistingUsername_ShouldThrowException() {
        // Arrange
        Role role1 = new Role(Roles.ROLE_CLIENT);
        SignUpCommand command = new SignUpCommand("user1", "password", List.of(role1));

        // Simulamos que el nombre de usuario ya existe
        when(userRepository.existsByUsername(command.username())).thenReturn(true);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userCommandService.handle(command);
        });

        // Act
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void handleSignUp_GivenRoleNotFound_ShouldThrowException() {
        // Arrange
        Role role1 = new Role(Roles.ROLE_CLIENT);
        Role role2 = new Role(Roles.ROLE_STAFF);
        SignUpCommand command = new SignUpCommand("user1", "password", List.of(role1, role2));

        when(userRepository.existsByUsername(command.username())).thenReturn(false);

        when(roleRepository.findByName(Roles.ROLE_CLIENT)).thenReturn(Optional.of(role1));
        when(roleRepository.findByName(Roles.ROLE_STAFF)).thenReturn(Optional.empty());  // Este rol no existe

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userCommandService.handle(command);
        });

        // Assert
        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    void handle_GivenCorrectCredentials_ShouldReturnUserAndToken() {
        // Arrange
        User user = new User("user1", "correctHashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        SignInCommand command = new SignInCommand("user1", "password");

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(user));
        when(hashingService.matches(command.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user.getUsername())).thenReturn("generatedToken");

        // Act
        Optional<ImmutablePair<User, String>> result = userCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get().getLeft());
        assertEquals("generatedToken", result.get().getRight());
    }

    @Test
    void handle_GivenNonExistingUser_ShouldThrowException() {
        // Arrange
        SignInCommand command = new SignInCommand("user1", "password");

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userCommandService.handle(command);
        });

        // Assert
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void handle_GivenInvalidPassword_ShouldThrowException() {
        // Arrange
        User user = new User("user1", "correctHashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        SignInCommand command = new SignInCommand("user1", "wrongPassword");

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(user));
        when(hashingService.matches(command.password(), user.getPassword())).thenReturn(false);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userCommandService.handle(command);
        });

        // Assert
        assertEquals("Invalid password", exception.getMessage());
    }
}