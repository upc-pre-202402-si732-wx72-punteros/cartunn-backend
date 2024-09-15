package com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform;

import com.thecoders.cartunnbackend.purchasing.domain.model.commands.CreateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.CreateOrderResource;

public class CreateOrderCommandFromResourceAssembler {

    public static CreateOrderCommand toCommandFromResource(CreateOrderResource resource) {
        return new CreateOrderCommand(resource.name(), resource.description(), resource.code(), resource.entryDate(), resource.exitDate(), resource.status());
    }
}