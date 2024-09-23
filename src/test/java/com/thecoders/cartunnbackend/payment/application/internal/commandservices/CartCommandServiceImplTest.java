package com.thecoders.cartunnbackend.payment.application.internal.commandservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreateCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreatePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.DeleteCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdateCartCommand;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.CartRepository;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartCommandServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartCommandServiceImpl cartCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateCartCommand() {
        Set<Product> products = new HashSet<>();
        CreatePaymentCommand createPaymentCommand = new CreatePaymentCommand("1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment(createPaymentCommand);
        CreateCartCommand command = new CreateCartCommand(new BigDecimal("100.0"), payment, products);
        Cart cart = new Cart(command);
        cart.setId(1L);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Long cartId = cartCommandService.handle(command);

        assertNotNull(cartId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void handleUpdateCartCommand() {
        Set<Product> products = new HashSet<>();
        CreatePaymentCommand createPaymentCommand = new CreatePaymentCommand("1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment(createPaymentCommand);
        UpdateCartCommand command = new UpdateCartCommand(1L, new BigDecimal("150.0"), payment, products);
        Cart cart = new Cart(new BigDecimal("100.0"), new Payment(createPaymentCommand), new HashSet<>());
        when(cartRepository.findById(command.id())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Optional<Cart> updatedCart = cartCommandService.handle(command);

        assertTrue(updatedCart.isPresent());
        verify(cartRepository, times(1)).findById(command.id());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void handleDeleteCartCommand() {
        DeleteCartCommand command = new DeleteCartCommand(1L);
        when(cartRepository.existsById(command.cartId())).thenReturn(true);

        cartCommandService.handle(command);

        verify(cartRepository, times(1)).existsById(command.cartId());
        verify(cartRepository, times(1)).deleteById(command.cartId());
    }
}