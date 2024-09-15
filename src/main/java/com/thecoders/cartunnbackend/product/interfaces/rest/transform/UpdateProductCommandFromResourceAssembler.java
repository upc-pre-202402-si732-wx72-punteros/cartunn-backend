package com.thecoders.cartunnbackend.product.interfaces.rest.transform;

import com.thecoders.cartunnbackend.product.domain.model.commands.UpdateProductCommand;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.UpdateProductResource;
public class UpdateProductCommandFromResourceAssembler {
    public static UpdateProductCommand toCommandFromResource(Long productId, UpdateProductResource resource) {
        return new UpdateProductCommand(productId, resource.title(), resource.description(), resource.image(), resource.price());
    }
}
