package com.thecoders.cartunnbackend.product.interfaces.rest;


import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteQueryService;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.FavoriteResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.FavoriteResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/products/{productId}/favorites", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Products")
public class ProductFavoritesController {
    private final FavoriteQueryService favoriteQueryService;
    public ProductFavoritesController(FavoriteQueryService favoriteQueryService) {
        this.favoriteQueryService = favoriteQueryService;
    }
    @GetMapping
    public ResponseEntity<List<FavoriteResource>> getAllFavoritesByProductId(@PathVariable Long productId) {
        var getAllFavoritesByProductIdQuery = new GetAllFavoritesByProductIdQuery(productId);
        var favorites = favoriteQueryService.handle(getAllFavoritesByProductIdQuery);
        var favoriteResources = favorites.stream().map(FavoriteResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(favoriteResources);
    }

}
