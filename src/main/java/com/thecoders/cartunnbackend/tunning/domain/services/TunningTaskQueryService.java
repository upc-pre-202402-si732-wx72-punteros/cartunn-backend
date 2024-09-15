package com.thecoders.cartunnbackend.tunning.domain.services;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetAllTunningTasksQuery;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetTunningTaskByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TunningTaskQueryService {

    Optional<TunningTask> handle(GetTunningTaskByIdQuery query);

    List<TunningTask> handle(GetAllTunningTasksQuery query);
}
