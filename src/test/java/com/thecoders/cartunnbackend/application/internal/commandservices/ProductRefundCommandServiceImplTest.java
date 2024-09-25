package com.thecoders.cartunnbackend.productRefunds.application.internal.commandservices;

import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.CreateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.UpdateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.infrastructure.jpa.persistence.ProductRefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductRefundCommandServiceImplTest {

    @Mock
    private ProductRefundRepository productRefundRepository;

    @InjectMocks
    private ProductRefundCommandServiceImpl productRefundCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateProductRefundCommand_GivenExistingTitle_ShouldThrowException() {
        // Arrange
        CreateProductRefundCommand command = new CreateProductRefundCommand("Refund 1", "Description", "NEW");
        when(productRefundRepository.existsByTitle(command.title())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productRefundCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Product refund with title Refund 1 already exists");
    }

    @Test
    void handleCreateProductRefundCommand_GivenValidCommand_ShouldSaveProductRefundAndReturnId() {
        // Arrange
        CreateProductRefundCommand command = new CreateProductRefundCommand("Refund 1", "Description", "NEW");
        ProductRefund productRefund = new ProductRefund(command);
        //productRefund.setId(1L); // Simulación de ID generado

        when(productRefundRepository.existsByTitle(command.title())).thenReturn(false);
        when(productRefundRepository.save(any(ProductRefund.class))).thenReturn(productRefund);

        // Act
        Long result = productRefundCommandService.handle(command);

        // Assert
        assertThat(result).isEqualTo(1L);
        verify(productRefundRepository, times(1)).save(any(ProductRefund.class));
    }

    @Test
    void handleCreateProductRefundCommand_WhenRepositoryThrowsException_ShouldThrowException() {
        // Arrange
        CreateProductRefundCommand command = new CreateProductRefundCommand("Refund 1", "Description", "NEW");

        when(productRefundRepository.existsByTitle(command.title())).thenReturn(false);
        when(productRefundRepository.save(any(ProductRefund.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productRefundCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while saving product refund: Database error");
    }

    @Test
    void handleUpdateProductRefundCommand_GivenTitleAlreadyExists_ShouldThrowException() {
        // Arrange
        UpdateProductRefundCommand command = new UpdateProductRefundCommand(1L, "Refund 1", "Updated Description", "NEW");
        when(productRefundRepository.existsByTitleAndIdIsNot(command.title(), command.id())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productRefundCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Profile with same product refund already exists");
    }

    @Test
    void handleUpdateProductRefundCommand_GivenNonExistingProductRefund_ShouldThrowException() {
        // Arrange
        UpdateProductRefundCommand command = new UpdateProductRefundCommand(1L, "Refund 1", "Updated Description", "NEW");
        when(productRefundRepository.findById(command.id())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productRefundCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("ProductRefund does not exist");
    }

    @Test
    void handleUpdateProductRefundCommand_GivenValidCommand_ShouldUpdateAndReturnUpdatedProductRefund() {
        // Arrange
        UpdateProductRefundCommand command = new UpdateProductRefundCommand(1L, "Refund 1", "Updated Description", "NEW");
        ProductRefund existingProductRefund = new ProductRefund("Refund 1", "Description", "NEW");
        //existingProductRefund.setId(1L);

        when(productRefundRepository.existsByTitleAndIdIsNot(command.title(), command.id())).thenReturn(false);
        when(productRefundRepository.findById(command.id())).thenReturn(Optional.of(existingProductRefund));
        when(productRefundRepository.save(any(ProductRefund.class))).thenReturn(existingProductRefund);

        // Act
        Optional<ProductRefund> result = productRefundCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(productRefundRepository, times(1)).save(existingProductRefund);
    }

    @Test
    void handleUpdateProductRefundCommand_WhenRepositoryThrowsException_ShouldThrowException() {
        // Arrange
        UpdateProductRefundCommand command = new UpdateProductRefundCommand(1L, "Refund 1", "Updated Description", "NEW");
        ProductRefund existingProductRefund = new ProductRefund("Refund 1", "Description", "NEW");
        //existingProductRefund.setId(1L);

        when(productRefundRepository.existsByTitleAndIdIsNot(command.title(), command.id())).thenReturn(false);
        when(productRefundRepository.findById(command.id())).thenReturn(Optional.of(existingProductRefund));
        when(productRefundRepository.save(any(ProductRefund.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productRefundCommandService.handle(command);
        });

        assertThat(exception.getMessage()).isEqualTo("Error while updating product refund: Database error");
    }
}
