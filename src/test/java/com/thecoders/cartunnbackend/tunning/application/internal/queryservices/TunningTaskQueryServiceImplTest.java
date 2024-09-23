package com.thecoders.cartunnbackend.tunning.application.internal.queryservices;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetAllTunningTasksQuery;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetTunningTaskByIdQuery;
import com.thecoders.cartunnbackend.tunning.infrastructure.persitence.jpa.repositories.TunningTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TunningTaskQueryServiceImplTest {

    @Mock
    private TunningTaskRepository tunningTaskRepository;

    @InjectMocks
    private TunningTaskQueryServiceImpl tunningTaskQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetAllTunningTasks_GivenCorrectQuery_ShouldReturnListOfTunningTasks() {
        // Arrange
        var query = new GetAllTunningTasksQuery();
        int expectedSize = 2;

        var tunningTask1 = new TunningTask();
        var tunningTask2 = new TunningTask();
        List<TunningTask> tunningTasks = List.of(tunningTask1, tunningTask2);

        when(tunningTaskRepository.findAll()).thenReturn(tunningTasks);

        // Act
        var result = tunningTaskQueryService.handle(query);

        // Assert
        assertEquals(expectedSize, result.size());
    }

    @Test
    void handleGetTunningTaskById_GivenExistingTunningTask_ShouldReturnTunningTask() {
        // Arrange
        long tunningTaskId = 1L;
        var query = new GetTunningTaskByIdQuery(tunningTaskId);
        var tunningTask = new TunningTask();

        when(tunningTaskRepository.findById(tunningTaskId)).thenReturn(Optional.of(tunningTask));

        // Act
        Optional<TunningTask> result = tunningTaskQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tunningTask, result.get());
    }
}