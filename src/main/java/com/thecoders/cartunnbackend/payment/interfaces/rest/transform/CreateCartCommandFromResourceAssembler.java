package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;

import com.thecoders.cartunnbackend.payment.domain.model.commands.CreateCartCommand;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.CreateCartResource;

public class CreateCartCommandFromResourceAssembler {
    public static CreateCartCommand toCommandFromResource(CreateCartResource resource) {
        return new CreateCartCommand(resource.total(),resource.payment(),resource.products());
    }
}
