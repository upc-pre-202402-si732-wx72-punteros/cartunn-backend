package com.thecoders.cartunnbackend.product.domain.services;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.CreateProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.UpdateProductCommand;

import java.util.Optional;

public interface ProductCommandService {
    Long handle(CreateProductCommand command);
    Optional<Product> handle(UpdateProductCommand command);

    void handle(DeleteProductCommand command);
}
