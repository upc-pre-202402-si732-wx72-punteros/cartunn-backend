package com.thecoders.cartunnbackend.payment.interfaces.rest;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.commands.*;
import com.thecoders.cartunnbackend.payment.domain.model.queries.*;
import com.thecoders.cartunnbackend.payment.domain.services.CartCommandService;
import com.thecoders.cartunnbackend.payment.domain.services.CartQueryService;
import com.thecoders.cartunnbackend.payment.domain.services.PaymentCommandService;
import com.thecoders.cartunnbackend.payment.domain.services.PaymentQueryService;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.*;
import com.thecoders.cartunnbackend.payment.interfaces.rest.transform.*;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetProductByIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.ProductCommandService;
import com.thecoders.cartunnbackend.product.domain.services.ProductQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartsControllerTest {

    @Mock
    private CartCommandService cartCommandService;

    @Mock
    private CartQueryService cartQueryService;

    @Mock
    private ProductCommandService productCommandService;

    @Mock
    private PaymentCommandService paymentCommandService;

    @Mock
    private ProductQueryService productQueryService;

    @Mock
    private PaymentQueryService paymentQueryService;

    @InjectMocks
    private CartsController cartsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCart() {
        RequestCartResource requestCartResource = new RequestCartResource(
                BigDecimal.valueOf(100.0),
                new CreatePaymentResource("1234567890123456", "12/23", "John Doe", "123", "credit"),
                Set.of(1L, 2L)
        );
        CreatePaymentCommand createPaymentCommand = new CreatePaymentCommand("1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");
        Set<Product> products = new HashSet<>(List.of(
                new Product("Product1", "Description1", "Image1", 10.0),
                new Product("Product2", "Description2", "Image2", 20.0)
        ));
        CreateCartResource createCartResource = new CreateCartResource(BigDecimal.valueOf(100.0), payment, products);
        CreateCartCommand createCartCommand = new CreateCartCommand(BigDecimal.valueOf(100.0), payment, products);

        when(paymentCommandService.handle(any(CreatePaymentCommand.class))).thenReturn(1L);
        when(paymentQueryService.handle(any(GetPaymentByIdQuery.class))).thenReturn(Optional.of(payment));
        when(productQueryService.handle(any(GetProductByIdQuery.class))).thenReturn(Optional.of(new Product("Product1", "Description1", "Image1", 10.0)));
        when(cartCommandService.handle(any(CreateCartCommand.class))).thenReturn(1L);
        when(cartQueryService.handle(any(GetCartByIdQuery.class))).thenReturn(Optional.of(new Cart(BigDecimal.valueOf(100.0), payment, products)));

        ResponseEntity<CartResource> response = cartsController.createCart(requestCartResource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getCart() {
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");
        Set<Product> products = new HashSet<>(List.of(new Product("Product1", "Description1", "Image1", 10.0)));
        Cart cart = new Cart(BigDecimal.valueOf(100.0), payment, products);

        when(cartQueryService.handle(any(GetCartByIdQuery.class))).thenReturn(Optional.of(cart));

        ResponseEntity<ResponseCartResource> response = cartsController.getCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllCarts() {
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");
        Set<Product> products = new HashSet<>(List.of(new Product("Product1", "Description1", "Image1", 10.0)));
        Cart cart = new Cart(BigDecimal.valueOf(100.0), payment, products);

        when(cartQueryService.handle(any(GetAllCartsQuery.class))).thenReturn(List.of(cart));

        ResponseEntity<List<ResponseCartResource>> response = cartsController.getAllCarts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateCart() {
        RequestUpdateCartResource requestUpdateCartResource = new RequestUpdateCartResource(
                BigDecimal.valueOf(150.0),
                new CreatePaymentResource("1234567890123456", "12/23", "John Doe", "123", "credit"),
                Set.of(1L, 2L)
        );
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");
        Set<Product> products = new HashSet<>(List.of(
                new Product("Product1", "Description1", "Image1", 10.0),
                new Product("Product2", "Description2", "Image2", 20.0)
        ));
        Cart cart = new Cart(BigDecimal.valueOf(100.0), payment, products);
        UpdateCartResource updateCartResource = new UpdateCartResource(BigDecimal.valueOf(150.0), payment, products);
        UpdateCartCommand updateCartCommand = new UpdateCartCommand(1L, BigDecimal.valueOf(150.0), payment, products);

        when(cartQueryService.handle(any(GetCartByIdQuery.class))).thenReturn(Optional.of(cart));
        when(productQueryService.handle(any(GetProductByIdQuery.class))).thenReturn(Optional.of(new Product("Product1", "Description1", "Image1", 10.0)));
        when(cartCommandService.handle(any(UpdateCartCommand.class))).thenReturn(Optional.of(cart));

        ResponseEntity<ResponseCartResource> response = cartsController.updateCart(1L, requestUpdateCartResource);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteCart() {
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");
        Set<Product> products = new HashSet<>(List.of(new Product("Product1", "Description1", "Image1", 10.0)));
        Cart cart = new Cart(BigDecimal.valueOf(100.0), payment, products);

        when(cartQueryService.handle(any(GetCartByIdQuery.class))).thenReturn(Optional.of(cart));

        ResponseEntity<?> response = cartsController.deleteCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cart deleted successfully", response.getBody());
    }
}