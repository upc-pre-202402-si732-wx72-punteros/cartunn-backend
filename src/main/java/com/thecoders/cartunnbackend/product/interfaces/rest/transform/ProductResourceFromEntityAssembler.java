package com.thecoders.cartunnbackend.product.interfaces.rest.transform;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.ProductResource;

public class ProductResourceFromEntityAssembler {
    public static ProductResource toResourceFromEntity(Product entity) {
        return new ProductResource(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getImage(), entity.getPrice());
    }
}
