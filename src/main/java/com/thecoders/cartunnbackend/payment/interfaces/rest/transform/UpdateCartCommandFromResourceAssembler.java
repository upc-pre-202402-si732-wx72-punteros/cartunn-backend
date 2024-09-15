package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;

import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdateCartCommand;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.UpdateCartResource;

public class UpdateCartCommandFromResourceAssembler {
    public static UpdateCartCommand toCommandFromResource(Long cartId, UpdateCartResource resource) {
        return new UpdateCartCommand(cartId, resource.total(),resource.payment(),resource.products());
    }
}
