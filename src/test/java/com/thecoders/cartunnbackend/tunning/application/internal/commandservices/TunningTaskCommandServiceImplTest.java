package com.thecoders.cartunnbackend.tunning.application.internal.commandservices;

import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.CreateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.DeleteTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.UpdateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.infrastructure.persitence.jpa.repositories.TunningTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TunningTaskCommandServiceImplTest {

    @Mock
    private TunningTaskRepository tunningTaskRepository;

    @InjectMocks
    private TunningTaskCommandServiceImpl tunningTaskCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateTunningTask_GivenCorrectCommand_ShouldReturnTunningId() {
        // Arrange
        var expectedId = 1L;
        var command = new CreateTunningTaskCommand(
                "modifiedPart",
                LocalDate.now(),
                "status");

        when(tunningTaskRepository.existsByModifiedPart(command.modifiedPart())).thenReturn(false);
        when(tunningTaskRepository.save(any(TunningTask.class))).thenAnswer(invocation -> {
            var savedTunningTask = invocation.getArgument(0, TunningTask.class);

            Field idField = AuditableAbstractAggregateRoot.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedTunningTask, expectedId);

            return savedTunningTask;
        });

        // Act
        var result = tunningTaskCommandService.handle(command);

        // Assert
        assertEquals(expectedId, result);
    }

    @Test
    void handleUpdateTunningTask_GivenCorrectCommand_ShouldReturnTunningTask() {
        // Arrange
        var command = new UpdateTunningTaskCommand(
                1L,
                "new part",
                LocalDate.now(),
                "new status");
        var tunningTask = new TunningTask(
                "past part",
                LocalDate.now(),
                "last status");

        when(tunningTaskRepository.existsByModifiedPartAndIdIsNot(command.modifiedPart(), command.id())).thenReturn(false);
        when(tunningTaskRepository.findById(command.id())).thenReturn(Optional.of(tunningTask));
        when(tunningTaskRepository.save(tunningTask)).thenReturn(tunningTask);

        // Act
        Optional<TunningTask> updatedTunningTask = tunningTaskCommandService.handle(command);

        // Assert
        assertTrue(updatedTunningTask.isPresent());
        assertEquals(command.modifiedPart(), updatedTunningTask.get().getModifiedPart());
        assertEquals(command.date(), updatedTunningTask.get().getDate());
        assertEquals(command.status(), updatedTunningTask.get().getStatus());
    }

    @Test
    void handleDeleteTunningTask_GivenExistingTunningTask_ShouldDeleteTunningTask() {
        // Arrange
        Long tunningTaskId = 1L;
        var command = new DeleteTunningTaskCommand(tunningTaskId);

        when(tunningTaskRepository.existsById(command.tunningTaskId())).thenReturn(true);

        // Act
        tunningTaskCommandService.handle(command);

        // Assert
        verify(tunningTaskRepository).deleteById(tunningTaskId);
    }
}