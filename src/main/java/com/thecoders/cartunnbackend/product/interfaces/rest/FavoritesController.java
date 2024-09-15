package com.thecoders.cartunnbackend.product.interfaces.rest;


import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllFavoritesQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetFavoriteByProductIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteCommandService;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteQueryService;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.FavoriteResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.RequestFavoriteResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.FavoriteResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.RequestFavoriteCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Favorites", description = "Products Favorite Management Endpoints")
public class FavoritesController {
    private final FavoriteCommandService favoriteCommandService;
    private final FavoriteQueryService favoriteQueryService;

    public FavoritesController(FavoriteCommandService favoriteCommandService, FavoriteQueryService favoriteQueryService) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteQueryService = favoriteQueryService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResource>> getAllFavorites() {
        var getAllFavoritesQuery = new GetAllFavoritesQuery();
        var favorites = favoriteQueryService.handle(getAllFavoritesQuery);
        var favoriteResources = favorites.stream().map(FavoriteResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(favoriteResources);
    }
    @PostMapping
    public ResponseEntity<FavoriteResource> requestFavorite(@RequestBody RequestFavoriteResource resource) {
        var command = RequestFavoriteCommandFromResourceAssembler.toCommandFromResource(resource);
        var favoriteId = favoriteCommandService.handle(command);
        System.out.println("Favorite ID = " + favoriteId);
        var getFavoriteByProductIdQuery = new GetFavoriteByProductIdQuery(resource.productId());
        var favorite = favoriteQueryService.handle(getFavoriteByProductIdQuery);
        if (favorite.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var favoriteResource = FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite.get(0));
        return new ResponseEntity<>(favoriteResource, HttpStatus.CREATED);
    }
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long favoriteId) {
        favoriteCommandService.handle(new DeleteFavoriteCommand(favoriteId));
        return ResponseEntity.ok("ProductFavorite deleted successfully");
    }

}

