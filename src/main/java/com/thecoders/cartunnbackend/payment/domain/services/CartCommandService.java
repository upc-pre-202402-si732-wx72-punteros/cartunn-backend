package com.thecoders.cartunnbackend.payment.domain.services;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreateCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.DeleteCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdateCartCommand;

import java.util.Optional;

public interface CartCommandService {
    Long handle(CreateCartCommand command);
    Optional<Cart> handle(UpdateCartCommand command);
    void handle(DeleteCartCommand command);
}