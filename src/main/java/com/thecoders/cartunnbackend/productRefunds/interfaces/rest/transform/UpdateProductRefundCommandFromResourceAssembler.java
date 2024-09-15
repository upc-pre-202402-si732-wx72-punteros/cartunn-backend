package com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform;

import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.UpdateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.UpdateProductRefundResource;


public class UpdateProductRefundCommandFromResourceAssembler {
    public static UpdateProductRefundCommand toCommandFromResource(Long productRefundId, UpdateProductRefundResource resource){
        return new UpdateProductRefundCommand(productRefundId, resource.title(), resource.description(), resource.status());

    }
}
