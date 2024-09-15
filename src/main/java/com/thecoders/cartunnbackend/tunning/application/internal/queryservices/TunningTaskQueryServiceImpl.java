package com.thecoders.cartunnbackend.tunning.application.internal.queryservices;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetAllTunningTasksQuery;
import com.thecoders.cartunnbackend.tunning.domain.model.queries.GetTunningTaskByIdQuery;
import com.thecoders.cartunnbackend.tunning.domain.services.TunningTaskQueryService;
import com.thecoders.cartunnbackend.tunning.infrastructure.persitence.jpa.repositories.TunningTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TunningTaskQueryServiceImpl implements TunningTaskQueryService {
    private final TunningTaskRepository tunningTaskRepository;
    public TunningTaskQueryServiceImpl(TunningTaskRepository tunningTaskRepository)
    {this.tunningTaskRepository = tunningTaskRepository;}
    @Override
    public Optional<TunningTask> handle(GetTunningTaskByIdQuery query) {
        return tunningTaskRepository.findById(query.tunningTaskId());
    }

    @Override
    public List<TunningTask> handle(GetAllTunningTasksQuery query) {
        return tunningTaskRepository.findAll();
    }
}
