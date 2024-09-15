package com.thecoders.cartunnbackend.product.interfaces.rest.transform;

import com.thecoders.cartunnbackend.product.domain.model.commands.CreateProductCommand;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.CreateProductResource;

public class CreateProductCommandFromResourceAssembler {

    public static CreateProductCommand toCommandFromResource(CreateProductResource resource) {
        return new CreateProductCommand(resource.title(), resource.description(), resource.image(), resource.price());
    }
}
