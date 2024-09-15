package com.thecoders.cartunnbackend.tunning.interfaces.rest;

import com.thecoders.cartunnbackend.tunning.domain.model.commands.DeleteTunningTaskCommand;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/tunning-task", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Tunning", description = "Tunning-Task Management Endpoints")

public class TunningTasksController {
    private final TunningTaskCommandService tunningTaskCommandService;
    private final TunningTaskQueryService tunningTaskQueryService;

    public TunningTasksController(TunningTaskCommandService tunningTaskCommandService, TunningTaskQueryService tunningTaskQueryService) {
        this.tunningTaskCommandService = tunningTaskCommandService;
        this.tunningTaskQueryService = tunningTaskQueryService;
    }
    @PostMapping
    public ResponseEntity<TunningTaskResource> createTunningTask(@RequestBody CreateTunningTaskResource createTunningTaskResource) {
        var createTunningTaskCommand = CreateTunningTaskCommandFromResourceAssembler.toCommandFromResource(createTunningTaskResource);
        var tunningTaskId = tunningTaskCommandService.handle(createTunningTaskCommand);
        if (tunningTaskId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getTunningTaskByIdQuery = new GetTunningTaskByIdQuery(tunningTaskId);
        var tunningTask = tunningTaskQueryService.handle(getTunningTaskByIdQuery);
        if (tunningTask.isEmpty()) return ResponseEntity.badRequest().build();
        var tunningTaskResource = TunningTaskResourceFromEntityAssembler.toResourceFromEntity(tunningTask.get());
        return new ResponseEntity<>(tunningTaskResource, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<TunningTaskResource>> getAllTunningTasks() {
        var getAllTunningTasksQuery = new GetAllTunningTasksQuery();
        var tunningTasks = tunningTaskQueryService.handle(getAllTunningTasksQuery);
        var tunningTaskResources = tunningTasks.stream().map(TunningTaskResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(tunningTaskResources);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TunningTaskResource> updateTunningTask(@PathVariable Long tunningTaskId, @RequestBody UpdateTunningTaskResource updateTunningTaskResource) {
        var updateTunningTaskCommand = UpdateTunningTaskCommandFromResourceAssembler.toCommandFromResource(tunningTaskId, updateTunningTaskResource);
        var updatedTunningTask = tunningTaskCommandService.handle(updateTunningTaskCommand);
        if (updatedTunningTask.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var tunningTaskResource = TunningTaskResourceFromEntityAssembler.toResourceFromEntity(updatedTunningTask.get());
        return ResponseEntity.ok(tunningTaskResource);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTunningTask(@PathVariable Long tunningTaskId) {
        var deleteTunningTaskCommand = new DeleteTunningTaskCommand(tunningTaskId);
        tunningTaskCommandService.handle(deleteTunningTaskCommand);
        return ResponseEntity.ok("TunningTask with given id successfully deleted");
    }

}
