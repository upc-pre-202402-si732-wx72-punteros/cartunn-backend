package com.thecoders.cartunnbackend.tunning.domain.services;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.CreateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.DeleteTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.domain.model.commands.UpdateTunningTaskCommand;

import java.util.Optional;

public interface TunningTaskCommandService {
    Long handle(CreateTunningTaskCommand command);
    Optional<TunningTask> handle(UpdateTunningTaskCommand command);
    void handle(DeleteTunningTaskCommand command);
}
