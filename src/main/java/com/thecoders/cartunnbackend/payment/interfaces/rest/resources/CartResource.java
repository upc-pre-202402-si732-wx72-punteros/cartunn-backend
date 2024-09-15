package com.thecoders.cartunnbackend.payment.interfaces.rest.resources;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;

import java.math.BigDecimal;
import java.util.Set;

public record CartResource(Long id, BigDecimal total, Payment payment, Set<Product> assignedProducts) {
}
