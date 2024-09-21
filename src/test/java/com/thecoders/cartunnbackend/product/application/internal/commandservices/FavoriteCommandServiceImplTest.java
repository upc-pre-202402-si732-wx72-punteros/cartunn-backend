package com.thecoders.cartunnbackend.product.application.internal.commandservices;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.RequestFavoriteCommand;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.FavoriteRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavoriteCommandServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FavoriteCommandServiceImpl favoriteCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateProduct_GivenCorrectCommand_ShouldReturnFavouriteId() {
        // Arrange
        Long productId = 1L;
        Long favoriteId = 100L;

        RequestFavoriteCommand command = new RequestFavoriteCommand(productId);
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(favoriteRepository.save(any(Favorite.class))).thenAnswer(invocation -> {
            Favorite savedFavorite = invocation.getArgument(0);

            Field idField = AuditableAbstractAggregateRoot.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedFavorite, favoriteId);
            return savedFavorite;
        });

        // Act
        Long result = favoriteCommandService.handle(command);

        // Assert
        assertEquals(favoriteId, result);
    }

    @Test
    void handleDeleteFavorite_GivenCorrectCommand_ShouldDeleteFavorite() {
        // Arrange
        Long favoriteId = 100L;
        DeleteFavoriteCommand command = new DeleteFavoriteCommand(favoriteId);
        when(favoriteRepository.existsById(favoriteId)).thenReturn(true);

        // Act
        favoriteCommandService.handle(command);

        // Assert
        assertAll(() -> verify(favoriteRepository).existsById(favoriteId));
        verify(favoriteRepository).deleteById(favoriteId);
    }
}