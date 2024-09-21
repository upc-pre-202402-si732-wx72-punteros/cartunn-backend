package com.thecoders.cartunnbackend.tunning.interfaces.rest;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.CreateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.DeleteTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.UpdateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetAllTunningTasksQuery;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetTunningTaskByIdQuery;
import com.thecoders.cartunnbackend.tunning.domain.services.TunningTaskCommandService;
import com.thecoders.cartunnbackend.tunning.domain.services.TunningTaskQueryService;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.resources.CreateTunningTaskResource;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.resources.TunningTaskResource;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.resources.UpdateTunningTaskResource;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.transform.CreateTunningTaskCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.transform.TunningTaskResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.transform.UpdateTunningTaskCommandFromResourceAssembler;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers=TunningTasksController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({MockitoExtension.class})
class TunningTasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TunningTaskCommandService tunningTaskCommandService;

    @MockBean
    private TunningTaskQueryService tunningTaskQueryService;

    private Long tunningTaskId;

    @BeforeEach
    void setUp() {
        tunningTaskId = 1L;
    }

    @Test
    void createTunningTask_GivenValidTunningTask_ShouldReturnCreatedStatusAndTunningTask() throws Exception {
        // Arrange
        CreateTunningTaskResource createTunningTaskResource = new CreateTunningTaskResource("Modified Part", LocalDate.now(), "In Progress");
        CreateTunningTaskCommand createTunningTaskCommand = new CreateTunningTaskCommand("Modified Part", LocalDate.now(), "In Progress");
        TunningTask tunningTask = new TunningTask("Modified Part", LocalDate.now(), "In Progress");
        TunningTaskResource tunningTaskResource = new TunningTaskResource(1L, "Modified Part", LocalDate.now(), "In Progress");

        try (MockedStatic<CreateTunningTaskCommandFromResourceAssembler> mockedAssembler = mockStatic(CreateTunningTaskCommandFromResourceAssembler.class);
             MockedStatic<TunningTaskResourceFromEntityAssembler> mockedAssembler2 = mockStatic(TunningTaskResourceFromEntityAssembler.class)) {
            mockedAssembler.when(() -> CreateTunningTaskCommandFromResourceAssembler.toCommandFromResource(createTunningTaskResource)).thenReturn(createTunningTaskCommand);
            mockedAssembler2.when(() -> TunningTaskResourceFromEntityAssembler.toResourceFromEntity(tunningTask)).thenReturn(tunningTaskResource);

            when(tunningTaskCommandService.handle(createTunningTaskCommand)).thenReturn(tunningTaskId);
            when(tunningTaskQueryService.handle(any(GetTunningTaskByIdQuery.class))).thenReturn(Optional.of(tunningTask));

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/tunning-task")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"modifiedPart\":\"Modified Part\",\"date\":\"" + LocalDate.now() + "\",\"status\":\"In Progress\"}"));

            // Assert
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.modifiedPart").value("Modified Part"))
                    .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                    .andExpect(jsonPath("$.status").value("In Progress"));
        }
    }

    @Test
    void getAllTunningTasks_GivenValidQuery_ShouldReturnOkAndListOfTunningTasks() throws Exception {
        // Arrange
        TunningTask tunningTask1 = new TunningTask("Modified Part 1", LocalDate.now(), "Completed");
        TunningTask tunningTask2 = new TunningTask("Modified Part 2", LocalDate.now().minusDays(1), "Pending");
        TunningTaskResource tunningTaskResource1 = new TunningTaskResource(1L, "Modified Part 1", LocalDate.now(), "Completed");
        TunningTaskResource tunningTaskResource2 = new TunningTaskResource(2L, "Modified Part 2", LocalDate.now().minusDays(1), "Pending");

        try (MockedStatic<TunningTaskResourceFromEntityAssembler> mockedAssembler = mockStatic(TunningTaskResourceFromEntityAssembler.class)) {
            mockedAssembler.when(() -> TunningTaskResourceFromEntityAssembler.toResourceFromEntity(tunningTask1)).thenReturn(tunningTaskResource1);
            mockedAssembler.when(() -> TunningTaskResourceFromEntityAssembler.toResourceFromEntity(tunningTask2)).thenReturn(tunningTaskResource2);
            when(tunningTaskQueryService.handle(any(GetAllTunningTasksQuery.class))).thenReturn(List.of(tunningTask1, tunningTask2));

            // Act
            ResultActions result = mockMvc.perform(get("/api/v1/tunning-task")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].modifiedPart").value("Modified Part 1"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].modifiedPart").value("Modified Part 2"));
        }
    }

    @Test
    void updateTunningTask_GivenValidTunningTask_ShouldReturnUpdatedTunningTask() throws Exception {
        // Arrange
        UpdateTunningTaskResource updateTunningTaskResource = new UpdateTunningTaskResource("Modified Part Updated", LocalDate.now(), "Completed");
        UpdateTunningTaskCommand updateTunningTaskCommand = new UpdateTunningTaskCommand(1L, "Modified Part Updated", LocalDate.now(), "Completed");
        TunningTask updatedTunningTask = new TunningTask("Modified Part Updated", LocalDate.now(), "Completed");
        TunningTaskResource updatedTunningTaskResource = new TunningTaskResource(1L, "Modified Part Updated", LocalDate.now(), "Completed");

        try (MockedStatic<UpdateTunningTaskCommandFromResourceAssembler> mockedAssembler = mockStatic(UpdateTunningTaskCommandFromResourceAssembler.class);
             MockedStatic<TunningTaskResourceFromEntityAssembler> mockedAssembler2 = mockStatic(TunningTaskResourceFromEntityAssembler.class)) {
            mockedAssembler.when(() -> UpdateTunningTaskCommandFromResourceAssembler.toCommandFromResource(1L, updateTunningTaskResource)).thenReturn(updateTunningTaskCommand);
            mockedAssembler2.when(() -> TunningTaskResourceFromEntityAssembler.toResourceFromEntity(updatedTunningTask)).thenReturn(updatedTunningTaskResource);
            when(tunningTaskCommandService.handle(updateTunningTaskCommand)).thenReturn(Optional.of(updatedTunningTask));

            // Act
            ResultActions result = mockMvc.perform(put("/api/v1/tunning-task/{tunningTaskId}", tunningTaskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"modifiedPart\":\"Modified Part Updated\",\"date\":\"" + LocalDate.now() + "\",\"status\":\"Completed\"}"));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.modifiedPart").value("Modified Part Updated"))
                    .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                    .andExpect(jsonPath("$.status").value("Completed"));
        }
    }

    @Test
    void deleteTunningTask_GivenValidId_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        DeleteTunningTaskCommand deleteTunningTaskCommand = new DeleteTunningTaskCommand(tunningTaskId);

        doNothing().when(tunningTaskCommandService).handle(deleteTunningTaskCommand);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/v1/tunning-task/{tunningTaskId}", tunningTaskId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string("TunningTask with given id successfully deleted"));
    }
}