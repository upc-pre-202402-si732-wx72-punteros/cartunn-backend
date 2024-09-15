package com.thecoders.cartunnbackend.product.domain.services;

import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.RequestFavoriteCommand;

public interface FavoriteCommandService {
    Long handle(RequestFavoriteCommand command);
    void handle(DeleteFavoriteCommand command);

}
