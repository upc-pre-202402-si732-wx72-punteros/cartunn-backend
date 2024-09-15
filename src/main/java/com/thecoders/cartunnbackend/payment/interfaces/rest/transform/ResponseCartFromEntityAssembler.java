package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.ResponseCartResource;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.ProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.ProductResourceFromEntityAssembler;

import java.util.HashSet;
import java.util.Set;

public class ResponseCartFromEntityAssembler {
    public static ResponseCartResource toResourceFromEntity(Cart entity) {
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(entity.getPayment());
        Set<ProductResource> productResources = new HashSet<>();
        for(Product product:entity.getAssignedProducts()){
            var productResource = ProductResourceFromEntityAssembler.toResourceFromEntity(product);
            productResources.add(productResource);
        }
        return new ResponseCartResource(entity.getId(), entity.getTotal(),paymentResource,productResources);
    }
}
