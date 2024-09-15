package com.thecoders.cartunnbackend.payment.application.internal.commandservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreateCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.DeleteCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdateCartCommand;
import com.thecoders.cartunnbackend.payment.domain.services.CartCommandService;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartCommandServiceImpl implements CartCommandService {
    private final CartRepository cartRepository;

    public CartCommandServiceImpl(CartRepository cartRepository) {this.cartRepository = cartRepository;}

    @Override
    public Long handle(CreateCartCommand command) {
        var cart = new Cart(command);
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving cart: " + e.getMessage());
        }
        return cart.getId();
    }
    @Override
    public Optional<Cart> handle(UpdateCartCommand command) {
        /*if (cartRepository.existsByTotalAndIdIsNot(command.total(), command.id()))
            throw new IllegalArgumentException("Cart with same price and id already exists");*/
        var result = cartRepository.findById(command.id());
        if (result.isEmpty()) throw new IllegalArgumentException("Cart does not exist");
        var cartToUpdate = result.get();
        try {
            var updatedCart = cartRepository.save(cartToUpdate.updateInformation(command.total(),
                    command.payment(),command.products()));
            return Optional.of(updatedCart);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating cart: " + e.getMessage());
        }
    }
    @Override
    public void handle(DeleteCartCommand command) {
        if (!cartRepository.existsById(command.cartId())) {
            throw new IllegalArgumentException("Cart does not exist");
        }
        try {
            cartRepository.deleteById(command.cartId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting cart: " + e.getMessage());
        }
    }


}
