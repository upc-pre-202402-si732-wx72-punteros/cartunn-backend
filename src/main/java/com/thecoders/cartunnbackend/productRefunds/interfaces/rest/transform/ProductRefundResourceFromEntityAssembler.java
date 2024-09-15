package com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform;


import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.ProductRefundResource;

public class ProductRefundResourceFromEntityAssembler {
    public static ProductRefundResource toResourceFromEntity(ProductRefund entity){
        return new ProductRefundResource(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus()
        );
    }
}
