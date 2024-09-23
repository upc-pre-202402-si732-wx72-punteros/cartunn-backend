package com.thecoders.cartunnbackend.product.interfaces.rest;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.RequestFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteCommandService;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteQueryService;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.FavoriteResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.RequestFavoriteResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.FavoriteResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.RequestFavoriteCommandFromResourceAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FavoritesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class FavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteCommandService favoriteCommandService;

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
    void getAllFavorites_GivenCorrectQuery_ShouldReturnOkAndListOfFavorites() throws Exception {
        // Arrange
        int expectedSize = 2;
        List<Favorite> favorites = List.of(favorite1, favorite2);

        try (MockedStatic<FavoriteResourceFromEntityAssembler> mockedAssembler = mockStatic(FavoriteResourceFromEntityAssembler.class)) {
            when(favoriteQueryService.handle(any(GetAllFavoritesQuery.class))).thenReturn(favorites);
            mockedAssembler.when(() -> FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite1)).thenReturn(favoriteResource1);
            mockedAssembler.when(() -> FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite2)).thenReturn(favoriteResource2);

            // Act
            ResultActions result = mockMvc.perform(get("/api/v1/favorites")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize))
                    .andExpect(jsonPath("$[0].favoriteId").value(favoriteId1))
                    .andExpect(jsonPath("$[0].productId").value(productId))
                    .andExpect(jsonPath("$[1].favoriteId").value(favoriteId2))
                    .andExpect(jsonPath("$[1].productId").value(productId));
        }
    }

    @Test
    void requestFavorite_GivenValidRequestFavorite_ShouldReturnCreatedAndFavorite() throws Exception {
        // Arrange
        var requestFavoriteResource = new RequestFavoriteResource(productId);
        var requestFavoriteCommand = new RequestFavoriteCommand(productId);
        var getFavoriteByProductIdQuery = new GetFavoriteByProductIdQuery(productId);
        var favoriteResource = new FavoriteResource(favoriteId1, productId);

        try (MockedStatic<RequestFavoriteCommandFromResourceAssembler> mockedCommandAssembler = mockStatic(RequestFavoriteCommandFromResourceAssembler.class);
             MockedStatic<FavoriteResourceFromEntityAssembler> mockedResourceAssembler = mockStatic(FavoriteResourceFromEntityAssembler.class)) {
            mockedCommandAssembler.when(() -> RequestFavoriteCommandFromResourceAssembler.toCommandFromResource(requestFavoriteResource))
                    .thenReturn(requestFavoriteCommand);

            when(favoriteCommandService.handle(requestFavoriteCommand)).thenReturn(favoriteId1);
            when(favoriteQueryService.handle(getFavoriteByProductIdQuery)).thenReturn(List.of(favorite1));

            mockedResourceAssembler.when(() -> FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite1)).thenReturn(favoriteResource);

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/favorites")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"productId\":1}"));

            // Assert
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.favoriteId").value(favoriteId1))
                    .andExpect(jsonPath("$.productId").value(productId));
        }
    }

    @Test
    void deleteFavorite_GivenValidFavoriteId_ShouldReturnOkAndDeleteMessage() throws Exception {
        // Arrange
        Long favoriteId = 1L;
        DeleteFavoriteCommand command = new DeleteFavoriteCommand(favoriteId);

        doNothing().when(favoriteCommandService).handle(command);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/v1/favorites/{favoriteId}", favoriteId));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string("ProductFavorite deleted successfully"));
    }
}