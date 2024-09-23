package com.thecoders.cartunnbackend.product.application.internal.commandservices;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.CreateProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.UpdateProductCommand;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.ProductRepository;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductCommandServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCommandServiceImpl productCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateProduct_GivenValidCommand_ShouldSaveProductAndReturnId() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand("Product Title", "Description", "Image", 100.0);

        when(productRepository.existsByTitle(command.title())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);

            Field idField = AuditableAbstractAggregateRoot.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedProduct, 1L);
            return savedProduct;
        });

        // Act
        Long productId = productCommandService.handle(command);

        // Assert
        assertTrue(productId>0);
    }

    @Test
    void handleCreateProduct_GivenDuplicateTitle_ShouldThrowException() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand("Duplicate Title", "Description", "image-url", 100.0);

        when(productRepository.existsByTitle(command.title())).thenReturn(true);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productCommandService.handle(command));

        // Assert
        assertEquals("Product with same title already exists", exception.getMessage());
    }

    @Test
    void handleUpdateProduct_GivenValidUpdateProductCommand_ShouldUpdateProduct() {
        // Arrange
        Product specificProduct = new Product("Laptop", "High-end gaming laptop", "image-url", 1999.99);
        UpdateProductCommand command = new UpdateProductCommand(specificProduct.getId(), "Laptop", "Updated description", "new-image-url", 2099.99);

        when(productRepository.existsByTitleAndIdIsNot(command.title(), command.id())).thenReturn(false);
        when(productRepository.findById(command.id())).thenReturn(Optional.of(specificProduct));
        when(productRepository.save(specificProduct)).thenReturn(specificProduct);

        // Act
        Optional<Product> updatedProduct = productCommandService.handle(command);

        // Assert
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated description", updatedProduct.get().getDescription());
        assertEquals("new-image-url", updatedProduct.get().getImage());
        assertEquals(2099.99, updatedProduct.get().getPrice());
    }

    @Test
    void handleUpdateProduct_GivenTitleAlreadyExists_ShouldThrowException() {
        // Arrange
        UpdateProductCommand command = new UpdateProductCommand(1L, "Laptop", "Updated description", "new-image-url", 2099.99);

        when(productRepository.existsByTitleAndIdIsNot(command.title(), command.id())).thenReturn(true);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productCommandService.handle(command));

        // Assert
        assertEquals("Product with same title already exists", exception.getMessage());
    }

    @Test
    void handleUpdateProduct_GivenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        UpdateProductCommand command = new UpdateProductCommand(1L, "Laptop", "Updated description", "new-image-url", 2099.99);

        when(productRepository.existsByTitleAndIdIsNot(command.title(), command.id())).thenReturn(false);
        when(productRepository.findById(command.id())).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productCommandService.handle(command));

        // Assert
        assertEquals("Product does not exist", exception.getMessage());
    }

    @Test
    void handleDeleteProduct_GivenExistingProduct_ShouldDeleteProduct() {
        // Arrange
        DeleteProductCommand command = new DeleteProductCommand(1L);

        when(productRepository.existsById(command.productId())).thenReturn(true);

        // Act & assert
        assertAll(() -> productCommandService.handle(command));
    }

    @Test
    void handleDeleteProduct_GivenNonExistingProduct_ShouldThrowException() {
        // Arrange
        DeleteProductCommand command = new DeleteProductCommand(1L);

        when(productRepository.existsById(command.productId())).thenReturn(false);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productCommandService.handle(command));

        // Assert
        assertEquals("Product does not exist", exception.getMessage());
    }
}