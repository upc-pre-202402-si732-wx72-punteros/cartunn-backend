package com.thecoders.cartunnbackend.product.application.internal.queryservices;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByIdQuery;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FavoriteQueryServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteQueryServiceImpl favoriteQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetFavorites_GivenCorrectQuery_ShouldReturnListOfFavorites() {
        // Arrange
        GetAllFavoritesQuery query = new GetAllFavoritesQuery();
        Favorite favorite1 = new Favorite();
        Favorite favorite2 = new Favorite();
        List<Favorite> favorites = List.of(favorite1, favorite2);
        int expectedSize = 2;

        when(favoriteRepository.findAll()).thenReturn(favorites);

        // Act
        List<Favorite> result = favoriteQueryService.handle(query);

        // Assert
        assertEquals(expectedSize, result.size());
    }

    @Test
    void handleGetFavoriteById_GivenCorrectFavoriteId_ShouldReturnFavorite() {
        // Arrange
        Long favoriteId = 1L;
        GetFavoriteByIdQuery query = new GetFavoriteByIdQuery(favoriteId);
        Favorite favorite = new Favorite();

        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));

        // Act
        Optional<Favorite> result = favoriteQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(favorite, result.get());
    }

    @Test
    void handleGetFavoritesByProductId_GivenCorrectProductId_ShouldReturnListOdFavorite() {
        // Arrange
        Long productId = 1L;
        List<Favorite> favoriteList = List.of(
                new Favorite(new Product("Product 1", "Description 1", "Image 1", 99.99)),
                new Favorite(new Product("Product 2", "Description 2", "Image 2", 59.99))
        );

        when(favoriteRepository.findAllByProductId(productId)).thenReturn(favoriteList);

        GetAllFavoritesByProductIdQuery query = new GetAllFavoritesByProductIdQuery(productId);

        // Act
        List<Favorite> result = favoriteQueryService.handle(query);

        // Assert
        assertEquals(favoriteList.size(), result.size());
        assertEquals(favoriteList, result);
    }

}