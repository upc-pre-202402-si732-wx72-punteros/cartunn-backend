package com.thecoders.cartunnbackend.tunning.interfaces.rest.transform;

import com.thecoders.cartunnbackend.tunning.domain.model.commands.UpdateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.resources.UpdateTunningTaskResource;

public class UpdateTunningTaskCommandFromResourceAssembler {
    public static UpdateTunningTaskCommand toCommandFromResource(Long tunningTaskId, UpdateTunningTaskResource resource){
        return new UpdateTunningTaskCommand(tunningTaskId, resource.modifiedPart(), resource.date(), resource.status());
    }
}
