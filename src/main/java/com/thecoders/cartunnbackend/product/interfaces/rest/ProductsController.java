package com.thecoders.cartunnbackend.product.interfaces.rest;

import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllProductsQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetProductByIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.ProductCommandService;
import com.thecoders.cartunnbackend.product.domain.services.ProductQueryService;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.CreateProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.UpdateProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.CreateProductCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.ProductResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.ProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.UpdateProductCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/products", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Products", description = "Product Management Endpoints")
public class ProductsController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public ProductsController(ProductCommandService productCommandService, ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }
    @PostMapping
    public ResponseEntity<ProductResource> createProduct(@RequestBody CreateProductResource createProductResource) {
        var createProductCommand = CreateProductCommandFromResourceAssembler.toCommandFromResource(createProductResource);
        var productId = productCommandService.handle(createProductCommand);
        if (productId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getProductByIdQuery = new GetProductByIdQuery(productId);
        var product = productQueryService.handle(getProductByIdQuery);
        if (product.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var productResource = ProductResourceFromEntityAssembler.toResourceFromEntity(product.get());
        return new ResponseEntity<>(productResource, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResource> getProduct(@PathVariable Long productId) {
        var getProductByIdQuery = new GetProductByIdQuery(productId);
        var product = productQueryService.handle(getProductByIdQuery);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var productResource = ProductResourceFromEntityAssembler.toResourceFromEntity(product.get());
        return ResponseEntity.ok(productResource);
    }
    @GetMapping
    public ResponseEntity<List<ProductResource>> getAllProducts() {
        var getAllProductsQuery = new GetAllProductsQuery();
        var products = productQueryService.handle(getAllProductsQuery);
        var productResources = products.stream().map(ProductResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(productResources);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResource> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductResource updateProductResource) {
        var updateProductCommand = UpdateProductCommandFromResourceAssembler.toCommandFromResource(productId, updateProductResource);
        var updatedProduct = productCommandService.handle(updateProductCommand);
        if (updatedProduct.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var productResource = ProductResourceFromEntityAssembler.toResourceFromEntity(updatedProduct.get());
        return ResponseEntity.ok(productResource);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        var deleteProductCommand = new DeleteProductCommand(productId);
        productCommandService.handle(deleteProductCommand);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
