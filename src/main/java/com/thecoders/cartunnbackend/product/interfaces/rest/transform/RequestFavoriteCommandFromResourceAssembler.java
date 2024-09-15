package com.thecoders.cartunnbackend.product.interfaces.rest.transform;

import com.thecoders.cartunnbackend.product.domain.model.commands.RequestFavoriteCommand;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.RequestFavoriteResource;

public class RequestFavoriteCommandFromResourceAssembler {

    public static RequestFavoriteCommand toCommandFromResource(RequestFavoriteResource resource) {
        return new RequestFavoriteCommand(resource.productId());
    }
}
