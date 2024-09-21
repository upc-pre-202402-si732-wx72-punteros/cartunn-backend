package com.thecoders.cartunnbackend.iam.interfaces.rest;

import com.thecoders.cartunnbackend.iam.domain.model.aggregates.User;
import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetAllUsersQuery;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetUserByIdQuery;
import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;
import com.thecoders.cartunnbackend.iam.domain.services.UserQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserQueryService userQueryService;

    @Test
    void getAllUsers_GivenValidQuery_ShouldReturnOkAndListOfUser() throws Exception {
        // Arrange
        User user1 = new User("user1", "hashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        User user2 = new User("user2", "hashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        List<User> users = List.of(user1, user2);

        when(userQueryService.handle(any(GetAllUsersQuery.class))).thenReturn(users);

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void getUserById_GivenValidUserId_ShouldReturnOkAndUser() throws Exception {
        // Arrange
        User user = new User("user1", "hashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.of(user));

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void getUserById_GivenInvalidUserId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(userQueryService.handle(any(GetUserByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());
    }
}