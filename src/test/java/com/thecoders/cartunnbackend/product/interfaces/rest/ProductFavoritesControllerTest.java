package com.thecoders.cartunnbackend.product.interfaces.rest;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteQueryService;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.FavoriteResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.FavoriteResourceFromEntityAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ProductFavoritesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProductFavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteQueryService favoriteQueryService;

    private Favorite favorite1;
    private Favorite favorite2;
    private FavoriteResource favoriteResource1;
    private FavoriteResource favoriteResource2;
    private long favoriteId1;
    private long favoriteId2;
    private long productId;

    @BeforeEach
    void setUp() {
        Product product = new Product("Product 1", "Description 1", "Image 1", 99.99);
        favorite1 = new Favorite(product);
        favorite2 = new Favorite(product);
        favoriteId1 = 1L;
        favoriteId2 = 2L;
        productId = 1L;
        favoriteResource1 = new FavoriteResource(favoriteId1, 1L);
        favoriteResource2 = new FavoriteResource(favoriteId2, 1L);
    }

    @Test
    void getAllFavoritesByProductId_GivenValidProductId_ShouldReturnOkAndFavoriteResources() throws Exception {
        // Arrange
        var favorites = List.of(favorite1, favorite2);
        var getAllFavoritesByProductIdQuery = new GetAllFavoritesByProductIdQuery(productId);

        try (MockedStatic<FavoriteResourceFromEntityAssembler> mockedAssembler = mockStatic(FavoriteResourceFromEntityAssembler.class)) {
            when(favoriteQueryService.handle(getAllFavoritesByProductIdQuery)).thenReturn(favorites);

            mockedAssembler.when(() -> FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite1)).thenReturn(favoriteResource1);
            mockedAssembler.when(() -> FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite2)).thenReturn(favoriteResource2);

            // Act
            ResultActions result = mockMvc.perform(get("/api/v1/products/{productId}/favorites", productId));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].favoriteId").value(favoriteId1))
                    .andExpect(jsonPath("$[0].productId").value(productId))
                    .andExpect(jsonPath("$[1].favoriteId").value(favoriteId2))
                    .andExpect(jsonPath("$[1].productId").value(productId));
        }

    }
}