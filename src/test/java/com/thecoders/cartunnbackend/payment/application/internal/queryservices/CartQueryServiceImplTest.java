package com.thecoders.cartunnbackend.payment.application.internal.queryservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetAllCartsQuery;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetCartByIdQuery;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartQueryServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartQueryServiceImpl cartQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetCartByIdQuery() {
        Long cartId = 1L;
        GetCartByIdQuery query = new GetCartByIdQuery(cartId);
        Cart cart = new Cart();
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Optional<Cart> result = cartQueryService.handle(query);

        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    void handleGetAllCartsQuery() {
        GetAllCartsQuery query = new GetAllCartsQuery();
        Cart cart = new Cart();
        List<Cart> carts = Collections.singletonList(cart);
        when(cartRepository.findAll()).thenReturn(carts);

        List<Cart> result = cartQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cart, result.get(0));
        verify(cartRepository, times(1)).findAll();
    }
}