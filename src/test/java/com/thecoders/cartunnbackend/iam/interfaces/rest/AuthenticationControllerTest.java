package com.thecoders.cartunnbackend.iam.interfaces.rest;

import com.thecoders.cartunnbackend.iam.domain.model.aggregates.User;
import com.thecoders.cartunnbackend.iam.domain.model.commands.SignInCommand;
import com.thecoders.cartunnbackend.iam.domain.model.commands.SignUpCommand;
import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;
import com.thecoders.cartunnbackend.iam.domain.services.UserCommandService;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.SignInResource;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.SignUpResource;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.UserResource;
import com.thecoders.cartunnbackend.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
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
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCommandService userCommandService;

    @Test
    void signUp_GivenValidResource_ShouldReturnUserResource() throws Exception {
        // Arrange
        SignUpResource signUpResource = new SignUpResource("user1", "password", List.of("ROLE_CLIENT"));
        User user = new User("user1", "hashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        SignUpCommand signUpCommand = new SignUpCommand("user1", "password", List.of(new Role(Roles.ROLE_CLIENT)));
        UserResource userResource = new UserResource(1L, "user1", List.of("ROLE_CLIENT"));

        try (MockedStatic<SignUpCommandFromResourceAssembler> mockedAssembler = mockStatic(SignUpCommandFromResourceAssembler.class);
             MockedStatic<UserResourceFromEntityAssembler> mockedUserAssembler = mockStatic(UserResourceFromEntityAssembler.class)) {

            mockedAssembler.when(() -> SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource))
                    .thenReturn(signUpCommand);

            when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.of(user));

            mockedUserAssembler.when(() -> UserResourceFromEntityAssembler.toResourceFromEntity(user))
                    .thenReturn(userResource);

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/authentication/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"user1\",\"password\":\"password\",\"roles\":[\"ROLE_CLIENT\"]}"));

            // Assert
            result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_CLIENT"));
        }
    }

    @Test
    void signUp_GivenExistingUsername_ShouldReturnBadRequestStatus() throws Exception {
        // Arrange
        SignUpResource signUpResource = new SignUpResource("existingUser", "password", List.of("ROLE_CLIENT"));
        SignUpCommand signUpCommand = new SignUpCommand("existingUser", "password", List.of(new Role(Roles.ROLE_CLIENT)));

        try (MockedStatic<SignUpCommandFromResourceAssembler> mockedAssembler = mockStatic(SignUpCommandFromResourceAssembler.class)) {

            mockedAssembler.when(() -> SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource))
                    .thenReturn(signUpCommand);

            when(userCommandService.handle(any(SignUpCommand.class))).thenReturn(Optional.empty());

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/authentication/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"existingUser\",\"password\":\"password\",\"roles\":[\"ROLE_CLIENT\"]}"));

            // Assert
            result.andExpect(status().isBadRequest());
        }
    }

    @Test
    void signIn_GivenValidResource_ShouldReturnAuthenticatedUser() throws Exception {
        // Arrange
        SignInResource signInResource = new SignInResource("user1", "password");
        SignInCommand signInCommand = new SignInCommand("user1", "password");
        User user = new User("user1", "hashedPassword", List.of(new Role(Roles.ROLE_CLIENT)));
        String token = "generatedToken";
        AuthenticatedUserResource authenticatedUserResource = new AuthenticatedUserResource(1L, "user1", token);

        try (MockedStatic<SignInCommandFromResourceAssembler> mockedAssembler = mockStatic(SignInCommandFromResourceAssembler.class);
             MockedStatic<AuthenticatedUserResourceFromEntityAssembler> mockedUserAssembler = mockStatic(AuthenticatedUserResourceFromEntityAssembler.class)) {

            mockedAssembler.when(() -> SignInCommandFromResourceAssembler.toCommandFromResource(signInResource))
                    .thenReturn(signInCommand);

            when(userCommandService.handle(any(SignInCommand.class)))
                    .thenReturn(Optional.of(ImmutablePair.of(user, token)));

            mockedUserAssembler.when(() -> AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(user, token))
                    .thenReturn(authenticatedUserResource);

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/authentication/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"user1\",\"password\":\"password\"}"));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.username").value("user1"))
                    .andExpect(jsonPath("$.token").value("generatedToken"));
        }
    }

    @Test
    void signIn_GivenInvalidUsername_ShouldReturnNotFoundStatus() throws Exception {
        // Arrange
        SignInResource signInResource = new SignInResource("invalidUser", "password");
        SignInCommand signInCommand = new SignInCommand("invalidUser", "password");

        try (MockedStatic<SignInCommandFromResourceAssembler> mockedAssembler = mockStatic(SignInCommandFromResourceAssembler.class)) {

            mockedAssembler.when(() -> SignInCommandFromResourceAssembler.toCommandFromResource(signInResource))
                    .thenReturn(signInCommand);

            when(userCommandService.handle(any(SignInCommand.class)))
                    .thenReturn(Optional.empty());

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/authentication/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"invalidUser\",\"password\":\"password\"}"));

            // Assert
            result.andExpect(status().isNotFound());
        }
    }

    @Test
    void signIn_GivenInvalidPassword_ShouldReturnNotFoundStatus() throws Exception {
        // Arrange
        SignInResource signInResource = new SignInResource("user1", "wrongPassword");
        SignInCommand signInCommand = new SignInCommand("user1", "wrongPassword");

        try (MockedStatic<SignInCommandFromResourceAssembler> mockedAssembler = mockStatic(SignInCommandFromResourceAssembler.class)) {

            mockedAssembler.when(() -> SignInCommandFromResourceAssembler.toCommandFromResource(signInResource))
                    .thenReturn(signInCommand);

            when(userCommandService.handle(any(SignInCommand.class)))
                    .thenReturn(Optional.empty());

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/authentication/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"user1\",\"password\":\"wrongPassword\"}"));

            // Assert
            result.andExpect(status().isNotFound());
        }
    }
}