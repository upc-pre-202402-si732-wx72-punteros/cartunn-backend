package com.thecoders.cartunnbackend.product.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record RequestFavoriteResource(
        @NotNull
        Long productId
) {
}