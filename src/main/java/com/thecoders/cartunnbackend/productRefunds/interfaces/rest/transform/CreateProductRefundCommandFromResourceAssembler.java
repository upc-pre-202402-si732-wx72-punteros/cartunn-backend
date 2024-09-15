package com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform;

import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.CreateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.CreateProductRefundResource;

public class CreateProductRefundCommandFromResourceAssembler {
    public static CreateProductRefundCommand toCommandFromResource(CreateProductRefundResource resource){
        return new CreateProductRefundCommand(
                resource.title(),
                resource.description(),
                resource.status()
        );
    }
}
