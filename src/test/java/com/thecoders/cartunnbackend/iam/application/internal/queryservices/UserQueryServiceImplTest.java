package com.thecoders.cartunnbackend.iam.application.internal.queryservices;

import com.thecoders.cartunnbackend.iam.domain.model.aggregates.User;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetAllUsersQuery;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetUserByIdQuery;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetUserByUsernameQuery;
import com.thecoders.cartunnbackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetAllUsers_GivenExistingUsers_ShouldReturnListOfUsers() {
        // Arrange
        GetAllUsersQuery query = new GetAllUsersQuery();
        List<User> users = List.of(new User("user1", "password1"), new User("user2", "password2"));
        when(userRepository.findAll()).thenReturn(users);

        int expectedListSize = 2;

        // Act
        List<User> result = userQueryService.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(expectedListSize, result.size());
    }

    @Test
    void handleGetAllUsers_GivenNonExistingUsers_ShouldReturnEmptyList() {
        // Arrange
        GetAllUsersQuery query = new GetAllUsersQuery();
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userQueryService.handle(query);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void handleGetUserById_GivenExistingUser_ShouldReturnUser() {
        // Arrange
        long userId = 1L;
        GetUserByIdQuery query = new GetUserByIdQuery(userId);
        User user = new User("user1", "password1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void handleGetUserById_GivenNonExistingUser_ShouldReturnEmptyOptional() {
        // Arrange
        long userId = 1L;
        GetUserByIdQuery query = new GetUserByIdQuery(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userQueryService.handle(query);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void handleGetUserByUsername_GivenExistingUser_ShouldReturnUser() {
        // Arrange
        String username = "user1";
        GetUserByUsernameQuery query = new GetUserByUsernameQuery(username);
        User user = new User(username, "password1");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void handleGetUserByUsername_GivenNonExistingUser_ShouldReturnEmptyOptional() {
        // Arrange
        String username = "user1";
        GetUserByUsernameQuery query = new GetUserByUsernameQuery(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        var result = userQueryService.handle(query);

        // Assert
        assertFalse(result.isPresent());
    }
}