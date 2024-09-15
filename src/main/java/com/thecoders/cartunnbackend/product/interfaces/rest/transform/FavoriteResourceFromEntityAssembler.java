package com.thecoders.cartunnbackend.product.interfaces.rest.transform;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.FavoriteResource;

public class FavoriteResourceFromEntityAssembler {
    public static FavoriteResource toResourceFromEntity(Favorite entity) {
        return new FavoriteResource(entity.getId(), entity.getProduct().getId());
    }
}
