package com.thecoders.cartunnbackend.iam.interfaces.rest;

import com.thecoders.cartunnbackend.iam.domain.model.entities.Role;
import com.thecoders.cartunnbackend.iam.domain.model.queries.GetAllRolesQuery;
import com.thecoders.cartunnbackend.iam.domain.model.valueobjects.Roles;
import com.thecoders.cartunnbackend.iam.domain.services.RoleQueryService;
import com.thecoders.cartunnbackend.iam.interfaces.rest.resources.RoleResource;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = RolesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RolesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleQueryService roleQueryService;

    @Test
    void getAllRoles_GivenValidQuery_ShouldReturnRoleResources() throws Exception {
        // Arrange
        Role role1 = new Role(Roles.ROLE_CLIENT);
        Role role2 = new Role(Roles.ROLE_STAFF);
        List<Role> roles = List.of(role1, role2);

        GetAllRolesQuery getAllRolesQuery = new GetAllRolesQuery();
        when(roleQueryService.handle(getAllRolesQuery)).thenReturn(roles);

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ROLE_CLIENT"))
                .andExpect(jsonPath("$[1].name").value("ROLE_STAFF"));
    }
}