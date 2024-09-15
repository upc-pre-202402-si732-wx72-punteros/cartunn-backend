package com.thecoders.cartunnbackend.productRefunds.domain.services;

import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.CreateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.UpdateProductRefundCommand;

import java.util.Optional;

public interface ProductRefundCommandService {
    Long handle(CreateProductRefundCommand command);
    Optional<ProductRefund> handle(UpdateProductRefundCommand command);
}
