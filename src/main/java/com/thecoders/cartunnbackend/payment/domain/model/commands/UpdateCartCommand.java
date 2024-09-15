package com.thecoders.cartunnbackend.payment.domain.model.commands;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateCartCommand(Long id, BigDecimal total, Payment payment, Set<Product> products) {
}
