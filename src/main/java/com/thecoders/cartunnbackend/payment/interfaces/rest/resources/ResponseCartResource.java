package com.thecoders.cartunnbackend.payment.interfaces.rest.resources;

import com.thecoders.cartunnbackend.product.interfaces.rest.resources.ProductResource;

import java.math.BigDecimal;
import java.util.Set;

public record ResponseCartResource(Long id, BigDecimal total, PaymentResource payment, Set<ProductResource> products) {
}
