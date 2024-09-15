package com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform;

import com.thecoders.cartunnbackend.purchasing.domain.model.commands.UpdateOrderCommand;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.UpdateOrderResource;

public class UpdateOrderCommandFromResourceAssembler {
    public static UpdateOrderCommand toCommandFromResource(Long orderId, UpdateOrderResource resource) {
        return new UpdateOrderCommand(orderId, resource.name(), resource.description(), resource.code(), resource.entryDate(), resource.exitDate(), resource.status());
    }
}