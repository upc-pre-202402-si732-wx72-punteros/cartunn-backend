package com.thecoders.cartunnbackend.productRefunds.application.internal.queryservices;

import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetAllProductRefundsQuery;
import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetProductRefundByIdQuery;
import com.thecoders.cartunnbackend.productRefunds.infrastructure.jpa.persistence.ProductRefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductRefundQueryServiceImplTest {

    @Mock
    private ProductRefundRepository productRefundRepository;

    @InjectMocks
    private ProductRefundQueryServiceImpl productRefundQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetProductRefundByIdQuery_GivenExistingProductRefund_ShouldReturnProductRefund() {
        // Arrange
        Long productRefundId = 1L;
        ProductRefund productRefund = new ProductRefund("Refund 1", "Description", "NEW");
        //productRefund.setId(productRefundId);

        when(productRefundRepository.findById(productRefundId)).thenReturn(Optional.of(productRefund));

        GetProductRefundByIdQuery query = new GetProductRefundByIdQuery(productRefundId);

        // Act
        Optional<ProductRefund> result = productRefundQueryService.handle(query);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productRefund);
        verify(productRefundRepository, times(1)).findById(productRefundId);
    }

    @Test
    void handleGetProductRefundByIdQuery_GivenNonExistingProductRefund_ShouldReturnEmptyOptional() {
        // Arrange
        Long productRefundId = 1L;

        when(productRefundRepository.findById(productRefundId)).thenReturn(Optional.empty());

        GetProductRefundByIdQuery query = new GetProductRefundByIdQuery(productRefundId);

        // Act
        Optional<ProductRefund> result = productRefundQueryService.handle(query);

        // Assert
        assertThat(result).isEmpty();
        verify(productRefundRepository, times(1)).findById(productRefundId);
    }

    @Test
    void handleGetAllProductRefundsQuery_GivenExistingProductRefunds_ShouldReturnListOfProductRefunds() {
        // Arrange
        ProductRefund productRefund1 = new ProductRefund("Refund 1", "Description 1", "NEW");
        ProductRefund productRefund2 = new ProductRefund("Refund 2", "Description 2", "PROCESSED");

        List<ProductRefund> productRefunds = List.of(productRefund1, productRefund2);

        when(productRefundRepository.findAll()).thenReturn(productRefunds);

        GetAllProductRefundsQuery query = new GetAllProductRefundsQuery();

        // Act
        List<ProductRefund> result = productRefundQueryService.handle(query);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactly(productRefund1, productRefund2);
        verify(productRefundRepository, times(1)).findAll();
    }

    @Test
    void handleGetAllProductRefundsQuery_GivenNoProductRefunds_ShouldReturnEmptyList() {
        // Arrange
        when(productRefundRepository.findAll()).thenReturn(List.of());

        GetAllProductRefundsQuery query = new GetAllProductRefundsQuery();

        // Act
        List<ProductRefund> result = productRefundQueryService.handle(query);

        // Assert
        assertThat(result).isEmpty();
        verify(productRefundRepository, times(1)).findAll();
    }
}
