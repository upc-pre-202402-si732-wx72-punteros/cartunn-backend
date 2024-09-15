package com.thecoders.cartunnbackend.tunning.interfaces.rest.transform;

import com.thecoders.cartunnbackend.tunning.domain.model.commands.CreateTunningTaskCommand;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.resources.CreateTunningTaskResource;

public class CreateTunningTaskCommandFromResourceAssembler {
    public static CreateTunningTaskCommand toCommandFromResource(CreateTunningTaskResource resource) {
        return new CreateTunningTaskCommand(resource.modifiedPart(),resource.date(),resource.status());
    }
}
