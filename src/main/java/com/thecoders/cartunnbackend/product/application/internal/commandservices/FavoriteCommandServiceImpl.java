package com.thecoders.cartunnbackend.product.application.internal.commandservices;

import com.thecoders.cartunnbackend.product.domain.exceptions.ProductNotFoundException;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Favorite;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.RequestFavoriteCommand;
import com.thecoders.cartunnbackend.product.domain.services.FavoriteCommandService;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.FavoriteRepository;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class FavoriteCommandServiceImpl implements FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;


    public FavoriteCommandServiceImpl(FavoriteRepository favoriteRepository,ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository= productRepository;
    }

    public Long handle(RequestFavoriteCommand command){
        Product product = productRepository.findById(command.productId()).orElseThrow(() -> new ProductNotFoundException(command.productId()));
        Favorite favorite = new Favorite( product);
        favorite = favoriteRepository.save(favorite);
        return favorite.getId();

    }
    @Override
    public void handle(DeleteFavoriteCommand command) {
        if (!favoriteRepository.existsById(command.favoriteId())) {
            throw new IllegalArgumentException("Favorite does not exist");
        }
        try {
            favoriteRepository.deleteById(command.favoriteId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting favorite: " + e.getMessage());
        }
    }

}
