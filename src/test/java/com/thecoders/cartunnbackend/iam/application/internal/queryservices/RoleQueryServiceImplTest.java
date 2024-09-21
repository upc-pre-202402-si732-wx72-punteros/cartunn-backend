package com.thecoders.cartunnbackend.iam.application.internal.queryservices;

import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetAllRolesQuery;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetRoleByNameQuery;
import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;
import com.thecoders.cartunnbackend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
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

class RoleQueryServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleQueryServiceImpl roleQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetAllRoles_GivenExistingRoles_ShouldReturnListOfRoles() {
        // Arrange
        var query = new GetAllRolesQuery();
        var roles = List.of(new Role(Roles.ROLE_CLIENT), new Role(Roles.ROLE_STAFF));
        when(roleRepository.findAll()).thenReturn(roles);
        var expectedListSize = 2;

        // Act
        var result = roleQueryService.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(expectedListSize, result.size());
    }

    @Test
    void handleGetAllRoles_GivenNonExistingRoles_ShouldReturnEmptyList() {
        // Arrange
        var query = new GetAllRolesQuery();
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = roleQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void handleGetRoleByName_GivenExistingRole_ShouldReturnRole() {
        // Arrange
        var query = new GetRoleByNameQuery(Roles.ROLE_CLIENT);
        var role = new Role(Roles.ROLE_CLIENT);
        when(roleRepository.findByName(Roles.ROLE_CLIENT)).thenReturn(Optional.of(role));

        // Act
        var result = roleQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(role, result.get());
    }

    @Test
    void handleGetRoleByName_GivenNonExistingRole_ShouldReturnEmptyOptional() {
        // Arrange
        var query = new GetRoleByNameQuery(Roles.ROLE_CLIENT);
        when(roleRepository.findByName(Roles.ROLE_CLIENT)).thenReturn(Optional.empty());

        // Act
        var result = roleQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
    }
}