package com.thecoders.cartunnbackend.payment.interfaces.rest;

import com.thecoders.cartunnbackend.payment.domain.model.commands.DeleteCartCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.DeletePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdatePaymentCommand;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/carts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Carts", description = "Cart Management Endpoints")
public class CartsController {
    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;
    private final ProductCommandService productCommandService;
    private final PaymentCommandService paymentCommandService;
    private final ProductQueryService productQueryService;
    private final PaymentQueryService paymentQueryService;

    public CartsController(CartCommandService cartCommandService,
                           CartQueryService cartQueryService,
                           ProductCommandService productCommandService,
                           PaymentCommandService paymentCommandService,
                           PaymentQueryService paymentQueryService,
                           ProductQueryService productQueryService) {
        this.cartCommandService = cartCommandService;
        this.cartQueryService = cartQueryService;
        this.productCommandService = productCommandService;
        this.paymentCommandService = paymentCommandService;
        this.productQueryService = productQueryService;
        this.paymentQueryService = paymentQueryService;
    }
    @PostMapping
    public ResponseEntity<CartResource> createCart(@RequestBody RequestCartResource requestCartResource) {
        var createPaymentCommand = CreatePaymentCommandFromResourceAssembler.toCommandFromResource(requestCartResource.payment());
        var paymentId = paymentCommandService.handle(createPaymentCommand);
        var getPaymentQuery = new GetPaymentByIdQuery(paymentId);
        var payment =  paymentQueryService.handle(getPaymentQuery);
        if (payment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Set<Product> products=new HashSet<>();
        for (Long id : requestCartResource.productIds()) {
            var getProductQuery = new GetProductByIdQuery(id);
            var product =  productQueryService.handle(getProductQuery);
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            else{
                products.add(product.get());
            }
        }
        CreateCartResource createCartResource = new CreateCartResource(requestCartResource.total(),
                payment.get(),products);
        var createCartCommand = CreateCartCommandFromResourceAssembler.toCommandFromResource(createCartResource);

        var cartId = cartCommandService.handle(createCartCommand);
        if (cartId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getCartByIdQuery = new GetCartByIdQuery(cartId);
        var cart = cartQueryService.handle(getCartByIdQuery);
        if (cart.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(cart.get());
        return new ResponseEntity<>(cartResource, HttpStatus.CREATED);
    }
    @GetMapping("/{cartId}")
    public ResponseEntity<ResponseCartResource> getCart(@PathVariable Long cartId) {
        var getCartByIdQuery = new GetCartByIdQuery(cartId);
        var cart = cartQueryService.handle(getCartByIdQuery);
        if (cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var responseCartResource = ResponseCartFromEntityAssembler.toResourceFromEntity(cart.get());
        return ResponseEntity.ok(responseCartResource);
    }
    @GetMapping
    public ResponseEntity<List<ResponseCartResource>> getAllCarts() {
        var getAllCartsQuery = new GetAllCartsQuery();
        var carts = cartQueryService.handle(getAllCartsQuery);
        var cartResources = carts.stream().map(ResponseCartFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(cartResources);
    }
    @PutMapping("/{cartId}")
    public ResponseEntity<ResponseCartResource> updateCart(@PathVariable Long cartId,
                                                           @RequestBody RequestUpdateCartResource requestUpdateCartResource) {
        Set<Product> products=new HashSet<>();
        for (Long id : requestUpdateCartResource.productIds()) {
            var getProductQuery = new GetProductByIdQuery(id);
            var product =  productQueryService.handle(getProductQuery);
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            else{
                products.add(product.get());
            }
        }
        var getCartByIdQuery = new GetCartByIdQuery(cartId);
        var cart = cartQueryService.handle(getCartByIdQuery);
        UpdateCartResource updateCartResource = new UpdateCartResource(requestUpdateCartResource.total(),
                cart.get().getPayment(),products);
        var updateCartCommand = UpdateCartCommandFromResourceAssembler.toCommandFromResource(cartId, updateCartResource);
        var updatedCart = cartCommandService.handle(updateCartCommand);
        if (updatedCart.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var updatePaymentCommand = new UpdatePaymentCommand(updatedCart.get().getPayment().getId(),
                requestUpdateCartResource.payment().cardNumber(),
                requestUpdateCartResource.payment().expirationDate(),
                requestUpdateCartResource.payment().cardHolder(),
                requestUpdateCartResource.payment().cvc(),
                requestUpdateCartResource.payment().methodPay());
        paymentCommandService.handle(updatePaymentCommand);
        var cartResource = ResponseCartFromEntityAssembler.toResourceFromEntity(updatedCart.get());
        return ResponseEntity.ok(cartResource);
    }
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable Long cartId) {
        var getCartByIdQuery = new GetCartByIdQuery(cartId);
        var cart = cartQueryService.handle(getCartByIdQuery);
        var deleteCartCommand = new DeleteCartCommand(cartId);
        cartCommandService.handle(deleteCartCommand);
        var deletePaymentCommand = new DeletePaymentCommand(cart.get().getPayment().getId());
        paymentCommandService.handle(deletePaymentCommand);
        return ResponseEntity.ok("Cart deleted successfully");
    }
    /*@GetMapping
    public ResponseEntity<List<CartProductResource>> getAllCartProducts() {
        var getAllCartProductsQuery = new GetAllCartProductsQuery();
        var carts = cartProductQueryService.handle(getAllCartProductsQuery);
        var cartProdutsResources = carts.stream().map(CartProductResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(cartProdutsResources);
    }
    @DeleteMapping("/{cartId}/{productId}")
    public ResponseEntity<?> deleteCartProduct(@PathVariable Long cartId, Long productId) {
        var deleteCartProductsCommand = new DeleteCartProductsCommand(cartId,productId);
        cartProductCommandService.handle(deleteCartProductsCommand);
        return ResponseEntity.ok("CartProduct deleted successfully");
    }
    @PostMapping
    public ResponseEntity<CartProductResource> createCartProduct(@RequestBody CreateCartProductResource createCartProductResource) {
        var createCartProductCommand = CreateCartProductCommandFromResourceAssembler.toCommandFromResource(createCartProductResource);
        CartProducts cartProducts = cartProductCommandService.handle(createCartProductCommand);
        if (cartProducts.getCartId() == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getCartProductByCartIdAndProductIdQuery = new GetCartProductbyCartIdAndProductIdQuery(cartProducts.getCartId(),cartProducts.getProductId());
        var cartProduct = cartProductQueryService.handle(getCartProductByCartIdAndProductIdQuery);
        if (cartProduct!=null) {
            return ResponseEntity.badRequest().build();
        }
        var cartProductResource = CartProductResourceFromEntityAssembler.toResourceFromEntity(cartProduct);
        return new ResponseEntity<>(cartProductResource, HttpStatus.CREATED);

    }*/
}
