package com.thecoders.cartunnbackend.product.application.internal.queryservices;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllProductsQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetProductByIdQuery;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductQueryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductQueryServiceImpl productQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetProducts_GivenExistingUsers_ShouldReturnListOfProducts() {
        // Arrange
        GetAllProductsQuery query = new GetAllProductsQuery();
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        int expectedSize = 2;

        // Act
        List<Product> result = productQueryService.handle(query);

        // Assert
        assertEquals(expectedSize, result.size());
    }

    @Test
    void handleGetProductsById_GivenExistingProduct_ShouldReturnProduct() {
        // Arrange
        long productId = 1;
        GetProductByIdQuery query = new GetProductByIdQuery(productId);
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> result = productQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }
}