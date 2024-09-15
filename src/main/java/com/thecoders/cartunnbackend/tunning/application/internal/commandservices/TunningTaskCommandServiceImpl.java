package com.thecoders.cartunnbackend.tunning.application.internal.commandservices;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.CreateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.DeleteTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.UpdateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.services.TunningTaskCommandService;
import com.thecoders.cartunnbackend.tunning.infrastructure.persitence.jpa.repositories.TunningTaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class TunningTaskCommandServiceImpl implements TunningTaskCommandService {
    private final TunningTaskRepository tunningTaskRepository;

    public TunningTaskCommandServiceImpl(TunningTaskRepository tunningTaskRepository) {
        this.tunningTaskRepository = tunningTaskRepository;
    }

    @Override
    public Long handle(CreateTunningTaskCommand command) {
        if (tunningTaskRepository.existsByModifiedPart(command.modifiedPart())) {
            throw new IllegalArgumentException("TunningTask with same title already exists");
        }
        var tunningTask = new TunningTask(command);
        try {
            tunningTaskRepository.save(tunningTask);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving product: " + e.getMessage());
        }
        return tunningTask.getId();
    }
    @Override
    public Optional<TunningTask> handle(UpdateTunningTaskCommand command) {
        if (tunningTaskRepository.existsByModifiedPartAndIdIsNot(command.modifiedPart(), command.id()))
            throw new IllegalArgumentException("Product with same title already exists");
        var result = tunningTaskRepository.findById(command.id());
        if (result.isEmpty()) throw new IllegalArgumentException("Product does not exist");
        var tunningTaskToUpdate = result.get();
        try {
            var updatedTunningTask = tunningTaskRepository.save(tunningTaskToUpdate.updateInformation(command.modifiedPart(), command.date(), command.status()));
            return Optional.of(updatedTunningTask);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating product: " + e.getMessage());
        }
    }
    @Override
    public void handle(DeleteTunningTaskCommand command) {
        if (!tunningTaskRepository.existsById(command.tunningTaskId())) {
            throw new IllegalArgumentException("Product does not exist");
        }
        try {
            tunningTaskRepository.deleteById(command.tunningTaskId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting product: " + e.getMessage());
        }
    }

}
