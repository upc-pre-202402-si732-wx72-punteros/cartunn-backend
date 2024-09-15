package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.CartResource;

public class CartResourceFromEntityAssembler {
    public static CartResource toResourceFromEntity(Cart entity) {

        return new CartResource(entity.getId(), entity.getTotal(),entity.getPayment(),entity.getAssignedProducts());
    }
}
