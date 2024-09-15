package com.thecoders.cartunnbackend.product.domain.services;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllProductsQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetProductByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProductQueryService {
    Optional<Product> handle(GetProductByIdQuery query);

    List<Product> handle(GetAllProductsQuery query);
}
